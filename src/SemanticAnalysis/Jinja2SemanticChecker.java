package SemanticAnalysis;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jinja2SemanticChecker {
    private static final Pattern FOR_PATTERN = Pattern.compile("\\{%\\s*for\\s+(\\w+)\\s+in\\s+(\\w+)\\s*%\\}");
    private static final Pattern ENDFOR_PATTERN = Pattern.compile("\\{%\\s*endfor\\s*%\\}");
    private static final Pattern IF_PATTERN = Pattern.compile("\\{%\\s*if\\s+.+?%\\}");
    private static final Pattern ENDIF_PATTERN = Pattern.compile("\\{%\\s*endif\\s*%\\}");
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+)\\s*\\}\\}");

    private final String templateSource;
    private final String templateName;
    private final Map<String, Object> contextData;

    public Jinja2SemanticChecker(String templateSource, String templateName, Map<String, Object> contextData) {
        this.templateSource = templateSource;
        this.templateName = templateName;
        this.contextData = contextData;
    }

    public List<SemanticError> check() {
        List<SemanticError> localErrors = new ArrayList<>();
        checkUnclosedBlocks(localErrors);
        checkContextVariables(localErrors);
        checkIterationTypes(localErrors);
        return localErrors;
    }

    private void checkUnclosedBlocks(List<SemanticError> errors) {
        Deque<String> blockStack = new ArrayDeque<>();
        Matcher forMatcher = FOR_PATTERN.matcher(templateSource);

        int searchStart = 0;
        while (true) {
            int forMatchStart = templateSource.indexOf("{% for ", searchStart);
            int endifMatchStart = templateSource.indexOf("{% endif ", searchStart);
            int nextBlock = templateSource.indexOf("{% endfor ", searchStart);

            int earliestFor = findNextMatch(templateSource, "\\{%\\s*for\\b", searchStart);

            if (earliestFor == -1) break;

            blockStack.push("for");
            searchStart = earliestFor + 1;
        }

        int endforCount = countMatches(templateSource, ENDFOR_PATTERN);
        int forCount = countMatches(templateSource, FOR_PATTERN);

        if (forCount != endforCount) {
            int unmatchedFor = forCount - endforCount;
            int lineNum = getLineNumber(templateSource, FOR_PATTERN.matcher(templateSource));
            errors.add(new SemanticError(
                    SemanticError.ErrorType.UNCLOSED_JINJA_BLOCK,
                    lineNum,
                    "For block opened but not closed: " + unmatchedFor + " unmatched {% for %} block(s)"
            ));
        }

        int ifCount = countMatches(templateSource, IF_PATTERN);
        int endifCount = countMatches(templateSource, ENDIF_PATTERN);
        if (ifCount != endifCount) {
            int unmatchedIf = ifCount - endifCount;
            int lineNum = getLineNumber(templateSource, "\\{%\\s*if\\b");
            errors.add(new SemanticError(
                    SemanticError.ErrorType.UNCLOSED_JINJA_BLOCK,
                    lineNum,
                    "If block opened but not closed: " + unmatchedIf + " unmatched {% if %} block(s)"
            ));
        }
    }

    private void checkContextVariables(List<SemanticError> errors) {
        Set<String> loopVars = new HashSet<>();
        Matcher forMatcher = FOR_PATTERN.matcher(templateSource);
        while (forMatcher.find()) {
            loopVars.add(forMatcher.group(1));
        }

        Matcher varMatcher = VARIABLE_PATTERN.matcher(templateSource);
        while (varMatcher.find()) {
            String varName = varMatcher.group(1);
            if (loopVars.contains(varName)) continue;
            if (contextData == null || !contextData.containsKey(varName)) {
                int lineNum = getLineNumber(templateSource, varMatcher.start());
                errors.add(new SemanticError(
                        SemanticError.ErrorType.UNDEFINED_CONTEXT_VARIABLE,
                        lineNum,
                        0,
                        "Variable '" + varName + "' used in template but not provided in context data"
                ));
            }
        }
    }

    private void checkIterationTypes(List<SemanticError> errors) {
        Matcher forMatcher = FOR_PATTERN.matcher(templateSource);
        while (forMatcher.find()) {
            String listVar = forMatcher.group(2);

            // Numeric literals (e.g. {% for i in 5 %}) are never iterable
            if (listVar.matches("-?\\d+(\\.\\d+)?")) {
                int lineNum = getLineNumber(templateSource, forMatcher.start());
                errors.add(new SemanticError(
                        SemanticError.ErrorType.INVALID_ITERATION_TYPE,
                        lineNum,
                        0,
                        "Cannot iterate over '" + listVar + "': numeric literal is not iterable"
                ));
                continue;
            }

            if (contextData == null || !contextData.containsKey(listVar)) {
                int lineNum = getLineNumber(templateSource, forMatcher.start());
                errors.add(new SemanticError(
                        SemanticError.ErrorType.UNDEFINED_CONTEXT_VARIABLE,
                        lineNum,
                        0,
                        "Cannot iterate over '" + listVar + "': variable not defined in context data"
                ));
                continue;
            }

            Object value = contextData.get(listVar);
            if (!(value instanceof List) && !(value instanceof Iterable) && !isString(value)) {
                int lineNum = getLineNumber(templateSource, forMatcher.start());
                errors.add(new SemanticError(
                        SemanticError.ErrorType.INVALID_ITERATION_TYPE,
                        lineNum,
                        0,
                        "Cannot iterate over '" + listVar + "': expected List/Iterable but found " + value.getClass().getSimpleName()
                ));
            }
        }
    }

    private boolean isString(Object value) {
        return value instanceof CharSequence || value instanceof String;
    }

    private int countMatches(String source, Pattern pattern) {
        Matcher m = pattern.matcher(source);
        int count = 0;
        while (m.find()) count++;
        return count;
    }

    private int getLineNumber(String source, int charPosition) {
        return source.substring(0, charPosition).split("\n").length;
    }

    private int getLineNumber(String source, Matcher matcher) {
        if (!matcher.find()) return 0;
        return getLineNumber(source, matcher.start());
    }

    private int getLineNumber(String source, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        if (m.find()) return getLineNumber(source, m.start());
        return 0;
    }

    private int findNextMatch(String source, String regex, int start) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        if (m.find(start)) return m.start();
        return -1;
    }
}
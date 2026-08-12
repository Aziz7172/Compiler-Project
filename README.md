# Flask Compiler — Java-based Python-to-HTML Compiler

## Overview
A Java-based compiler that parses Python Flask application source code (`.py`) and Jinja2 HTML templates (`.jinja`), performs semantic analysis, executes a virtual machine to populate runtime context, and renders pure HTML output files via AST traversal (Visitor Pattern).

## Project Structure
```
Flask_Compiler-master/
├── app.py              # Main Flask application source
├── templates/          # Jinja2 template input files
│   ├── index.jinja
│   ├── products.jinja
│   ├── add_product.jinja
│   ├── edit_product.jinja
│   └── product_details.jinja
├── src/                # Java source files
│   ├── Main.java
│   ├── CompilerPipeline.java
│   ├── TemplateManager.java
│   ├── AST/            # AST node definitions and builder
│   ├── VM/             # Virtual machine executor and bytecode generator
│   ├── SemanticAnalysis/ # Semantic error detection
│   ├── SymbolTable/    # Flask route tracking
│   ├── CodeGenerator/  # Python bytecode generation (Visitor Pattern)
│   ├── HtmlCodeGenerator/ # HTML generation via AST Visitor Pattern
│   ├── Jinja2/         # Jinja2 AST builder and node definitions
│   ├── ErrorHandling/  # Compiler error listener
│   └── ANTLR/          # ANTLR parser grammars
├── output/             # Generated HTML files and static assets
└── compiler_output/    # JSON exports, semantic report, generation log
```

## Building and Running
```bash
# Compile
javac -d out -sourcepath src -cp "antlr-4.13.1-complete.jar" src/**/*.java

# Run (with project directory as argument)
java -cp "out;src;antlr-4.13.1-complete.jar" Main .

# Run single-file test mode (AST print + semantic analysis only, no HTML generation)
java -cp "out;src;antlr-4.13.1-complete.jar" Main tests/test_type_error.py
```

## Architecture / Pipeline

The compiler follows a **static-site-generator** architecture with two parallel processing tracks.

### Track 1: Python Processing (`app.py`)

```
app.py → PythonLexer (ANTLR) → PythonParser (ANTLR) → Python AST → SemanticAnalyzer → PythonContextExtractor (VM) → Context Map
```

1. **Lexing & Parsing**: `app.py` is tokenized by `PythonLexer` and parsed by `PythonParser` (ANTLR-generated) into a parse tree.
2. **AST Construction**: `ASTBuilder` visits the parse tree and builds a typed AST (`ProgramNode`, `AssignmentNode`, `BinaryOpNode`, `ForNode`, etc.).
3. **Semantic Analysis**: `SemanticAnalyzer` traverses the AST with a `SymbolTable` scope stack, detecting 8 error types:
   - `UNDEFINED_VARIABLE`, `TYPE_MISMATCH`, `DUPLICATE_FLASK_ROUTE`, `RETURN_OUTSIDE_FUNCTION`, `FUNCTION_ARGUMENT_MISMATCH`, `UNDEFINED_CONTEXT_VARIABLE`, `UNCLOSED_JINJA_BLOCK`, `INVALID_ITERATION_TYPE`
4. **Context Extraction**: `PythonContextExtractor` (from the `VM` package) walks the Python AST and populates a `Context` map with runtime values — variables, function return values, lists (e.g. `products`, `students`), and Flask config flags.

### Track 2: Jinja2 Template Processing (`templates/*.jinja`)

```
templates/*.jinja → JinjaLexer (ANTLR) → JinjaParser (ANTLR) → Jinja AST → HtmlCodeGenerator (Visitor Pattern using Context Map) → output/*.html
```

1. **Lexing & Parsing**: Each `.jinja` file is tokenized and parsed by `JinjaLexer`/`JinjaParser` into a Jinja2 parse tree.
2. **AST Construction**: `Jinja2ASTBuilder` builds a `Jinja2Node` tree from the parse tree.
3. **HTML Generation (Visitor Pattern — NOT Regex)**: `HtmlCodeGenerator` traverses the Jinja2 AST using the **Visitor Pattern** — no regex-based rendering. It uses the global `Context` map to resolve variable values at compile time and produces pure HTML output. Supported Jinja2 constructs:
   - `{{ variable }}` — variable substitution (resolved from Context)
   - `{{ variable.attr }}` — attribute access on context objects
   - `{% for item in list %}...{% endfor %}` — loop iteration
   - `{% if condition %}...{% else %}...{% endif %}` — conditional rendering

### Final Output

All template outputs are written to `output/*.html`. A `compiler_output/` directory contains `ast_python.json`, `ast_jinja.json`, `semantic_report.txt`, and `generation_log.txt`.

## Generated Output
After running the pipeline, the following is produced:

### `output/` directory
- Pure HTML files rendered from Jinja2 templates (`.html`)
- Static assets copied verbatim (`app.py`)

### `compiler_output/` directory
- `ast_python.json` — Python AST in JSON (every node has `lineNumber` and `columnNumber`)
- `ast_jinja.json` — Jinja2 template AST in JSON
- `semantic_report.txt` — Semantic errors in format `[Line: X, Col: Y] (ERROR_TYPE): message`
- `generation_log.txt` — Timestamped log of all pipeline steps

## Features
- Semantic analysis detecting 8 error types (UNDEFINED_VARIABLE, TYPE_MISMATCH, DUPLICATE_FLASK_ROUTE, RETURN_OUTSIDE_FUNCTION, FUNCTION_ARGUMENT_MISMATCH, UNDEFINED_CONTEXT_VARIABLE, UNCLOSED_JINJA_BLOCK, INVALID_ITERATION_TYPE)
- VM execution populating Context with runtime values (variables, function return values, product lists)
- Auto-injection of `product` from `products` context for details page rendering
- Clean string values (quotes stripped from Python string literals)
- Jinja2 template rendering with for-loops, if/else, variable substitution, and attribute access
- HTML generation via AST Visitor Pattern (not regex)
- Pure HTML output with no remaining Jinja2 tags
- Consistent navigation bar across all templates for smooth page transitions
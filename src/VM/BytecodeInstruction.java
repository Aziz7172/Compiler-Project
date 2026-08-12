package VM;

public class BytecodeInstruction {
    private final String opcode;
    private final String operand;
    private final String valueOperand;
    private final int intOperand;
    private final Object objectOperand;

    public BytecodeInstruction(String opcode) {
        this(opcode, "", "", 0, null);
    }

    public BytecodeInstruction(String opcode, String operand) {
        this(opcode, operand, "", 0, null);
    }

    public BytecodeInstruction(String opcode, int intOperand) {
        this(opcode, "", "", intOperand, null);
    }

    public BytecodeInstruction(String opcode, String operand, int intOperand) {
        this(opcode, operand, "", intOperand, null);
    }

    public BytecodeInstruction(String opcode, String operand, String valueOperand) {
        this(opcode, operand, valueOperand, 0, null);
    }

    public BytecodeInstruction(String opcode, String operand, String valueOperand, int intOperand) {
        this(opcode, operand, valueOperand, intOperand, null);
    }

    public BytecodeInstruction(String opcode, String operand, String valueOperand, int intOperand, Object objectOperand) {
        this.opcode = opcode;
        this.operand = operand;
        this.valueOperand = valueOperand;
        this.intOperand = intOperand;
        this.objectOperand = objectOperand;
    }

    public BytecodeInstruction(String opcode, String operand, Object objectOperand) {
        this(opcode, operand, "", 0, objectOperand);
    }

    public String getOpcode() { return opcode; }
    public String getOperand() { return operand; }
    public String getValueOperand() { return valueOperand; }
    public int getIntOperand() { return intOperand; }
    public Object getObjectOperand() { return objectOperand; }

    @Override
    public String toString() {
        if (operand.isEmpty() && valueOperand.isEmpty() && intOperand == 0 && objectOperand == null) return opcode;
        if (objectOperand != null) return opcode + " " + operand + "=" + objectOperand;
        if (!valueOperand.isEmpty()) return opcode + " " + operand + "=" + valueOperand;
        if (intOperand != 0) return opcode + " " + operand + " " + intOperand;
        return opcode + " " + operand;
    }
}
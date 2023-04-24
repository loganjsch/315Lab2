// Logan Schwarz
// Garrett Green
import java.util.HashMap;

public class instruction {
    public String asm;

    private static final HashMap<String, String> asmToBinary =  new HashMap<String, String>(){{
        put("and", "000000");
        put("or", "000000");
        put("add", "000000");
        put("addi", "001000");
        put("sll", "000000");
        put("sub", "000000");
        put("slt", "000000");
        put("beq", "000100");
        put("bne", "000101");
        put("lw", "100011");
        put("sw", "101011");
        put("j", "000010");
        put("jr", "000000");
        put("jal", "000011");
        put("$zero", "00000");
        put("$v0", "00010");
        put("$v1", "00011");
        put("$a0", "00100");
        put("$a1", "00101");
        put("$a2", "00110");
        put("$a3", "00111");
        put("$t0", "01000");
        put("$t1", "01001");
        put("$t2", "01010");
        put("$t3", "01011");
        put("$t4", "01100");
        put("$t5", "01101");
        put("$t6", "01110");
        put("$t7", "01111");
        put("$s0", "10000");
        put("$s1", "10001");
        put("$s2", "10010");
        put("$s3", "10011");
        put("$s4", "10100");
        put("$s5", "10101");
        put("$s6", "10110");
        put("$s7", "10111");
        put("$t8", "11000");
        put("$t9", "11001");
        put("$sp", "11101");
        put("$ra", "11111");
        put("$0", "00000");
    }};

    private static final HashMap<String, String> functMap = new HashMap<String, String>() {{
        put("and", "100100");
        put("or", "100101");
        put("add", "100000");
        put("sll", "000000");
        put("sub", "100010");
        put("slt", "101010");
        put("jr", "001000");
    }};

    public instruction(String asm) {  
        this.asm = asm;
    }
    

    public String returnBinary(){

        StringBuilder binaryOut = new StringBuilder();
        String[] parts = asm.split("\\s+");
        switch (parts[0]) {
            // All r type follow the same instructions 
            case "add":
            case "sub":
            case "or":
            case "and":
            case "slt":
                binaryOut.append(asmToBinary.get(parts[0]) + " ");
                binaryOut.append(asmToBinary.get(parts[2]) + " ");
                binaryOut.append(asmToBinary.get(parts[3])+ " ");
                binaryOut.append(asmToBinary.get(parts[1])+ " ");
                binaryOut.append("00000"+ " ");
                binaryOut.append(functMap.get(parts[0]));
                break;

            // Immediate
            case "addi":
                binaryOut.append(asmToBinary.get(parts[0]) + " ");
                binaryOut.append(asmToBinary.get(parts[2])+ " ");
                binaryOut.append(asmToBinary.get(parts[1])+ " ");
                int immediate = Integer.parseInt(parts[3]);
                String binaryImmediate = Integer.toBinaryString(immediate);
                String paddedImmediate = String.format("%16s", binaryImmediate).replace(' ', '0');
                paddedImmediate = paddedImmediate.substring(paddedImmediate.length() - 16);
                binaryOut.append(paddedImmediate);
                break;

            // Jr
            case "jr":
                binaryOut.append(asmToBinary.get(parts[0]) + " ");
                binaryOut.append(asmToBinary.get(parts[1]) + " ");
                binaryOut.append("00000 ");
                binaryOut.append("00000 ");
                binaryOut.append("00000 ");
                binaryOut.append(functMap.get(parts[0]));
                break;

            case "sll":
                binaryOut.append(asmToBinary.get(parts[0])+ " 00000 ");
                binaryOut.append(asmToBinary.get(parts[2])+ " ");
                binaryOut.append(asmToBinary.get(parts[1])+ " ");
                int sllimm = Integer.parseInt(parts[3]);
                String binsllim = Integer.toBinaryString(sllimm);
                String paddedbinsllim = String.format("%5s", binsllim).replace(' ', '0');
                binaryOut.append(paddedbinsllim + " ");
                binaryOut.append(asmToBinary.get(parts[0]));
                break;

            case "j":
            case "jal":
                binaryOut.append(asmToBinary.get(parts[0]) + " ");
                break;       
            case "beq":
            case "bne":
                binaryOut.append(asmToBinary.get(parts[0]) + " ");
                binaryOut.append(asmToBinary.get(parts[1]) + " ");
                binaryOut.append(asmToBinary.get(parts[2]) + " ");
                break;

            // I type 
            case "lw":
            case "sw":
                binaryOut.append(asmToBinary.get(parts[0])+ " ");
                binaryOut.append(asmToBinary.get(parts[3])+ " ");
                binaryOut.append(asmToBinary.get(parts[1])+ " ");
                int ItypeImmediate = Integer.parseInt(parts[2]);
                String binaryItypeImmediate = Integer.toBinaryString(ItypeImmediate);
                String padddedbinitypeimm = String.format("%16s", binaryItypeImmediate).replace(' ', '0');
                binaryOut.append(padddedbinitypeimm);
                break;

            default:
                binaryOut.append("invalid instruction: " + parts[0]);
        }
        String binStr = binaryOut.toString();
        return binStr;
    }



}
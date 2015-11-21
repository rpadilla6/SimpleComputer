
import java.io.*;
import java.util.*;

/**
 *  Emulator
 *  @author rpadilla6
 *  This is where I will write my first novel(maybe)
 */
public class Emulator {
    /*          
            m)	The opcodes
            n)	Using the program
            */
    
    private final String[] mem;
    private ArrayList<String> IC;  //Input Cards is dynamic array of ints
    private int ICcount;            // To iterate through input cards
    private ArrayList<String> OC;  
    private int AC;
    private int PC;
    private String IR;    //IR is better off as a string, i.e. instruction 001 != 1
    String status;        //Used for multiple statuses
    final String outputCards = "C:\\Users\\queso\\Workspace\\Emulator\\src\\outputCards";
    final String inputCards = "C:\\Users\\queso\\Workspace\\Emulator\\src\\inputCards";
        
    /**
     * Initializes memory with 100 cells, then fills memory and cpu with default
     * values. It also initializes the input and output card arrays, and sets
     * the status to gucci.
     */
    Emulator(){
        // Initialize the memory
        mem = new String[100];
        ClearMem();
        //Input/Output Card things
        IC = new ArrayList<>();
        OC = new ArrayList<>();
        ICcount = 0;
        //Initially set status to arbitrary value(not keyword)
        status = "gucci";
    }

    /**
     *
     * @param args
     */
        public static void main(String[] args) {
        
        // Create Emulator!                
        Scanner in = new Scanner(System.in);
        Emulator Sc = new Emulator();
        
        //Display Menu
        Sc.Menu();
        
        
        //Will continue to run until otherwise specified
        while(!Sc.status.matches("ex")){
            System.out.println("Enter Keyword: ");        
            Sc.status = in.nextLine();
            Sc.status = Sc.status.toLowerCase();
            switch(Sc.status){
                case "ld":  System.out.println("Enter filename: ");
                            Sc.status = in.nextLine();
                            try{
                                Sc.ReadFromFile(Sc.status);
                            }
                            catch(FileNotFoundException x){
                                System.out.println("No such file!");
                                break;
                            }
                            catch(IOException ex){
                                System.out.println("Bad Input");
                                break;
                            }
                            System.out.println("File Loaded Successfully!");
                            break;
                case "sv":  try{
                                    System.out.println("Enter Filename to Save program: ");
                                    Sc.WriteToFile(in.next());
                                }
                                catch(IOException ex){
                                    System.out.println("Error writing to file");
                                    break;
                                }
                            System.out.println("File Saved Successfully!");
                            break;
                case "txt": System.out.println("Enter cell#, then program to be input to be"
                            + "sequentially entered into memory: ");
                            Sc.ReadFromStdin(in.nextLine());
                            System.out.println("Memory updated!");
                            break;
                case "ics": System.out.println("Enter input cards: ");
                            Sc.ReadInputCardsFromStdin(in.nextLine());
                            System.out.println("Input cards updated!");
                            break;
                case "icf": try{
                                System.out.println("Enter filename: ");
                                Sc.ReadInputCardsFromFile(Sc.inputCards);
                            }
                            catch(FileNotFoundException x){
                                System.out.println("Unable to open file");
                                break;
                            }
                            catch(IOException ex){
                                System.out.println("Bad input");
                                break;
                            }
                            System.out.println("Input cards updated!");
                            break;
                case "pc":  System.out.print("Set Program Counter to cell #: ");
                            Sc.SetPC(in.nextInt());
                            in.nextLine();
                            System.out.println("Program Counter set to " + Sc.PC);
                            break;
                case "run": while(!Sc.status.matches("halt")){
                                Sc.ExecuteInstruction();
                            }
                            System.out.println("Program executed!");
                            break;
                case "stp": int step = 0;
                            while(!Sc.status.matches("halt")){
                                Sc.ExecuteInstruction();
                                System.out.println("After step " + ++step + ", cpu contents are as follows:");
                                System.out.println(Sc.Step());
                                System.out.println("Press enter to continue");
                                in.nextLine();
                            }
                            System.out.println("Program executed!");
                            break;
                case "cla": Sc.ClearAll();
                            System.out.println("All clear!");
                            break;
                case "clm": Sc.ClearMem();
                            System.out.println("Memory cleared!");
                            break;
                case "cli": Sc.ClearIC();
                            System.out.println("Input cards cleared!");
                            break;
                case "clo": Sc.ClearOC();
                            System.out.println("Output cards cleared!");
                            break;
                case "ps":  Sc.DisplayContent();
                            break;
                case "pf":  try{
                                System.out.println("Enter filename: ");
                                Sc.SaveContent(in.nextLine());
                            }
                            catch(IOException ex){
                                System.out.println("Tengo un problemo");
                                break;
                            }
                            System.out.println("Output saved!");
                            break;
                case "mnu": Sc.Menu();
                            break;
                case "ex":  System.out.println("Adios amigo");
                            break;
                default :   System.out.println("No comprendo muchacho");
                            break;
            }
        }
    }
    
    /**
     *  This method prints a menu to terminal showing user keywords in order
     * to operate the emulator
     */
    private void Menu(){
        String separator = "----------------------------------------------"
                + "-----------";
        System.out.println(separator);
        System.out.println("\tWelcome to the Simple Computer Emulator!");
        System.out.println(separator);
        System.out.println("Keywords\tDescription\n********\t~~~~~~~~~~~");
        System.out.println("ld\t\tLoads program from file");
        System.out.println("sv\t\tSaves program to file");
        System.out.println("txt\t\tEnter program from stdin");
        System.out.println("ics\t\tEnter input cards from stdin");
        System.out.println("icf\t\tEnter input cards from file");
        System.out.println("pc\t\tSet program counter");
        System.out.println("run\t\tRun program");
        System.out.println("stp\t\tStep through program");
        System.out.println("cla\t\tClear all memory/input/output cards");
        System.out.println("clm\t\tClear memory");
        System.out.println("cli\t\tClear input cards");
        System.out.println("clo\t\tClear output cards");
        System.out.println("ps\t\tPrint memory components to screen");
        System.out.println("pf\t\tPrint memory components to file");
        System.out.println("mnu\t\tRe-Display menu");
        System.out.println("ex\t\tExit program");
        System.out.println(separator);
    }
    
    /**
    *   This method sets the initial value of the program counter. May wish to set
    *   restrictions on values later
    */    
    private void SetPC(int n){
        PC = n;
    }
    /**
     * Save program to file. Method should be run before execution of simple
     * computer program.
     * @param filename
     * @throws IOException 
     */
    private void WriteToFile(String filename) throws IOException{
        FileWriter fWriter = new FileWriter(filename);
        try(BufferedWriter writer = new BufferedWriter(fWriter)){
            for (int i = 1; i < mem.length; i++) {
                if(!mem[i].matches("000")){
                    writer.write(i + " " + mem[i] + "\n");
                }
            }
        }
    }
    /**
     * Creates a string representation of memory.<p>
     * It's pretty dank.
     * 
     * @return a string
     */
    private String MemToString(){
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            out.append(i<10 ? "0"+i : i)
                    .append(mem[i].charAt(0) == '-' ? ":" : ": ")
                    .append(mem[i]).append("\t\t").append(i+20)
                    .append(mem[i+20].charAt(0) == '-' ? ":" : ": ")
                    .append(mem[i+20]).append("\t\t").append(i+40)
                    .append(mem[i+40].charAt(0) == '-' ? ":" : ": ")
                    .append(mem[i+40]).append("\t\t").append(i+60)
                    .append(mem[i+60].charAt(0) == '-' ? ":" : ": ")
                    .append(mem[i+60]).append("\t\t").append(i+80)
                    .append(mem[i].charAt(0) == '-' ? ":" : ": ")
                    .append(mem[i+80]).append("\n");
        }
        return out.toString();
    }
    /**
    *This method writes formatted output of mem cells to a file called out.text. 
    *<strike>If a GUI is implemented, there will be another output method.</strike>
    */
    private void SaveContent(String location) throws IOException {
        FileWriter fWriter = new FileWriter(location);
        try(BufferedWriter writer = new BufferedWriter(fWriter)){
            writer.write("Input Cards\t\t:" + this.IC.toString() + "\n");
            writer.write("Output Cards\t\t:" + this.OC.toString() + "\n");
            writer.write(this.Step() + "\n");
            writer.write(this.MemToString());
        }        
    }
    /**
    * This method reads input from standard input file. 
    * The input will be placed
    * in memory starting from the cell indicated by the user. Therefore, input
    * must be formatted as /d{1-2} /d{1-3} /d{1-3} /d{1-3} ... {/d{1-3}\n
    * <p>
    * WARNING: if memory input is greater than 3 signed decimal digits, this 
    * method will only store the 3 least significant digits (signed)
    */
    private void ReadFromStdin(String input){
        Scanner s = new Scanner(input);
        int start = s.nextInt();
        for (int i = start; i < mem.length && s.hasNext(); i++) {
            if(i == 0){
                System.out.println(s.next() + " cannot be stored in cell 0!");
                continue;
            }
            mem[i] = this.Converter(Integer.parseInt(s.next()));
        }
    }
    /**
    *This method reads input from a file, formatted as "/d/d /d/d/d/n"
    *For testing purpose, the filename is stored in a string defined in main,
    *but this can be changed to standard input. As far as I can tell, the full
    *file name is needed in order for this to work
    * 
    * @param fileName
    * @exception FileNotFoundException
    * @exception IOException
    */
    private void ReadFromFile(String fileName) throws FileNotFoundException, IOException {
        String line;
        FileReader fReader = new FileReader(fileName);
        try (BufferedReader reader = new BufferedReader(fReader)) {
            while(reader.ready()){
                line = reader.readLine();
                String[] temp = line.split(" ");
                if(temp[0].matches("0")){
                    System.out.print("No writing in cell 0");
                    continue;
                }
                mem[Integer.parseInt(temp[0])] = temp[1];
            }
        }
    }
    /**
     * This method reads the input cards from standard input
     * It is formatted as /d /d /d ... /d\n 
     * <p>
     * The numbers will be read through the scanner and stored in the dynamic
     * string array for Input Cards
     * 
     * @param input 
     */
    private void ReadInputCardsFromStdin(String input){
        Scanner s = new Scanner(input);
        while(s.hasNext()){
            IC.add(this.Converter(Integer.parseInt(s.next())));
        }
    }
    /**
    *This method reads the input cards from a file, formatted as "/d+/n"
    *For testing purposes, the filename is stored in a string variable defined at
    *the beginning of main. As it is with the other file reading method, the full
    *extension is needed in order for this to work.
    */
    private void ReadInputCardsFromFile(String fileName) throws FileNotFoundException, IOException{
        FileReader fReader = new FileReader(fileName);
        try (BufferedReader reader = new BufferedReader(fReader)) {
            while (reader.ready()){
                IC.add(reader.readLine());
            }
        }
    }
    /**
     * This is the meat of the program right here. The switch methods inside
     * will run the actual simple computer program, at least it should.<p>
     * There are more detailed descriptions as you traverse the switch statement
     * inside the method, but basically this method does 10 things:
     * <ol start="0">
     * <li>Input an element from an input card into the listed memory cell</li>
     * <li>Output the contents of a specific cell to an output card</li>
     * <li>Add contents of stated memory cell to the accumulator</li>
     * <li>Subtract contents of indicated memory cell from the accumulator</li>
     * <li>Set the contents of the accumulator equal to the contents of specified cell number</li>
     * <li>Store the value of the accumulator in the listed cell number</li>
     * <li>Jump to the indicated memory cell, but store the program counter in cell 99</li>
     * <li>Jump to indicated memory cell if the accumulator is negative, if not, proceed as usual</li>
     * <li>Shift the contents of the accumulator x units left, y units right</li>
     * <li>Halt the program after setting program counter to corresponding cell number</li>
     * </ol>
     * @throws IndexOutOfBoundsException 
     */
    private void ExecuteInstruction() throws IndexOutOfBoundsException{
        IR = mem[PC];
        PC++;
        String instruction = IR.charAt(0) + "";
        int address = Integer.parseInt(IR.substring(1));
        
        switch(instruction){
            /*
            INPut - Copy the input card into cell number _ _ , and advance the input device
            to the next card. If the input card is blank, then advance the input device, set
            the contents of the program counter to 00, and halt the SC processor.
            */
            case "0":   if(address == 0){
                            System.out.println("Cannot execute command on cell 0");
                            System.exit(0);
                        }
                        String tmp = "";
                        try{
                            tmp = IC.get(ICcount++);
                            
                            if(tmp.length()>3){
                                if(tmp.length()==4&&tmp.charAt(0)=='-'){
                                    break;
                                }
                                System.out.println("Input cannot be greater than"
                                        + "three signed decimal digits!");
                                IC.remove(ICcount-1);   //Remove the infidel
                            }
                        }
                        catch(IndexOutOfBoundsException x){
                            System.out.println("Not enough Input Cards!!!");
                            this.status = "halt";
                            break;
                        }
                        mem[address] = tmp;    
                        break;
            /*
            OUTput - Copy the contents of cell number _ _ onto the output card and advance
            the output device one card.
            */
            case "1":   OC.add(mem[address]);
                        break;
            /*
            ADD - Add the contents of cell number _ _ to the value of the accumulator.
            */
            case "2":   AC += Integer.parseInt(mem[address]);
                        break;
            /*
            SUBtract - Subtract the contents of cell number _ _ from the value of the
            accumulator
            */
            case "3":   AC -= Integer.parseInt(mem[address]);
                        break;
            /*
            LoaD Accumulator - Clear the accumulator and copy the contents of cell number
            _ _ into the accumulator.
            */
            case "4":   AC = Integer.parseInt(mem[address]);
                        break;
            /*
            STore Accumulator - Copy the least significant three digits of the accumulator
            into cell number _ _ .
            */
            case "5":   if(address == 0){
                            System.out.println("Cannot execute command on cell 0");
                            System.exit(0);
                        }
                        mem[address] = this.Converter(AC);
                        break;
            /*
            JuMP - Place the present value of the program counter into cell 99. Then,
            change the value of the program counter to correspond to cell number _ _ .
            */
            case "6":   mem[99] = this.Converter(PC);
                        PC = address;
                        break;
            /*
            Test ACcumulator - If the value of the accumulator is negative, change the
            value of the program counter to correspond to that of cell number _ _ .
            */
            case "7":   if(AC<0)PC=address;
                        break;
            /*
            SHiFt - Shift the accumulator left x digits, and then shift the result right y
            digits. With all left shifts, zeros enter the accumulator on the right. Similarly,
            with right shifts, zeros will enter the accumulator on the left.
            */
            case "8":   AC = this.Shift(IR.charAt(1), IR.charAt(2));
                        break;
            /*
            HaLT - Replace the value of the program counter with cell number _ _ and
            then halt the SC processor.
            */
            case "9":   this.SetPC(address);
                        status = "halt";
                        break;
            //In case nothing matches (program input file is wrong)
            default:    System.out.println("idk what u did fam");
                        break;
        }
    }
    /**
    *This method writes output cards to a file, might just be the most useless
    *method of the program. For the moment it will just sit here, alone and 
    *forgotten...
    */
    private void WriteOutputCards () throws IOException{
        try(BufferedWriter wr = new BufferedWriter(new FileWriter(outputCards))){
            for (String n : OC) {
                wr.write(n);
            }
        }
        catch(IOException x){
            System.out.println("Ya m8 there was a problem");
        }
    }
    
    /** (Copied description from calling function)
    *SHiFt - Shift the accumulator left x digits, and then shift the result right y
    *digits. With all left shifts, zeros enter the accumulator on the right. Similarly,
    *with right shifts, zeros will enter the accumulator on the left.
    *<p>
    *This method preforms shifts on the accumulator and returns an integer value
    *for the accumulator instead of directly changing it (a little safer)
    */
    private int Shift(char a, char b){
        int x = Integer.parseInt(a + "");
        int y = Integer.parseInt(b + "");
        String str = AC + "";        
        for (int i = 0; i < x; i++) {
            str = str + "0";
        }
        if(str.length()>4){
            str = str.substring(str.length()-4);
        }
        for (int j = 0; j < y; j++) {
            str = "0" + str;
        }
        if(str.length()>4){
            str = str.substring(str.length()-4);
        }
        return Integer.parseInt(str);
    }
    
    /**
    *This method exists to convert integers into the proper format for storing
    *in the string based memory. Remember,  the memory is formatted to store
    *three signed decimal digits.
    */
    private String Converter (int var){
        String str;
        if(Math.abs(var)<10){
            str = "00" + Math.abs(var);
        }
        else if(Math.abs(var)<100){
            str = "0" + Math.abs(var);
        }
        else {
            str = Math.abs(var) + "";
            str = str.substring(str.length()-3);
        }
        if(var<0){
            str="-" + str;
        }
        return str;
    }
    
    /**
     * This method resets all memory cells, accumulator, and instruction 
     * register to the original state
     */
    private void ClearMem(){
        for (int i = 1; i < mem.length; i++) {
            mem[i] = "000";
        }
        mem[0] = "001";
        AC = PC =0;
        IR = "0";
    }
    
    /**
     * This method empties the Input Card dynamic array (sets iterator to zero)
     */ 
    private void ClearIC(){
        IC.clear();
        ICcount = 0;
    }
    
    /** This method empties the Output Card dynamic array
     */
    private void ClearOC(){
        OC.clear();
    }
    
    /**
     * This method clears memory, input and output cards;
     */
    private void ClearAll(){
        ClearMem();
        ClearIC();
        ClearOC();
    }

    /**
     *<strike>Used for testing only</strike>, this method will output whatever is in the input
     *cards and output cards. It will also output the value of the accumulator,
     * instruction register, and program counter. It will also dump whatever is
     *in memory to the standard output file and the terminal.
     */
    
    private void DisplayContent(){
        System.out.println("-------------------------------------------------"
                + "----------------------");
        System.out.println("Input Cards:\t\t" + this.IC.toString());
        System.out.println("Output Cards:\t\t" + this.OC.toString());
        System.out.print(this.Step());
        System.out.println("-------------------------------------------------"
                + "----------------------");
        System.out.println(this.MemToString());
    }
    /**
     * This method produces a string representation of the CPU.<p>
     * It's also pretty dank.
     * 
     * @return a string
     */
    private String Step(){
        return "Accumulator:\t\t" + AC + "\nInstruction Register:\t" + IR +
                "\nProgram Counter:\t" + PC + "\n";
    }
}

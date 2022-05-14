import java.io.*;
import java.util.*;

class G {
    static void Output( FileInformation fileInf, ArrayList< ArrayList<Character> > ganttArray
                        , ArrayList< ArrayList< ProcessTime> > processTimes ) throws Throwable {
        fileInf.fileName.replace( ".txt", "" );
        fileInf.fileName = "out_" + fileInf.fileName + ".txt" ;
        FileWriter fileWriter = new FileWriter( fileInf.fileName ) ;
        BufferedWriter writer = new BufferedWriter( fileWriter ) ;

        ArrayList<Character> tmpGantt ;
        String methodName = "";
        if( fileInf.method == 1 ) {
            methodName = "FCFS";
        }
        else if( fileInf.method == 2 ) {
            methodName = "RR";
        }
        else if( fileInf.method == 3 ) {
            methodName = "SRTF";
        }
        else if( fileInf.method == 4 ) {
            methodName = "PPRR";
        }
        else if( fileInf.method == 5 ) {
            methodName = "HRRN";
        }

        if( fileInf.method != 6 ) {
            writer.write( methodName + "\n" );
            tmpGantt = ganttArray.get(0);
            for( int i = 0 ; i < tmpGantt.size() ; i++ ) {
                writer.write( String.valueOf( tmpGantt.get(i) ) ) ;
            }
            writer.write( "\n" ) ;

        }
        else {
            writer.write( "All\n" ) ;

            for( int j = 0 ; j < ganttArray.size() ; j++ ) {
                if( j == 0 ) {
                    writer.write( "==        FCFS==\n" ) ;
                }
                else if( j == 1 ) {
                    writer.write( "==          RR==\n" ) ;
                }
                else if( j == 2 ) {
                    writer.write( "==        SRTF==\n" ) ;
                }
                else if( j == 3 ) {
                    writer.write( "==        PPRR==\n" ) ;
                }
                else if( j == 4 ) {
                    writer.write( "==        HRRN==\n" ) ;
                }

                tmpGantt = ganttArray.get(j);
                for( int i = 0 ; i < tmpGantt.size() ; i++ ) {
                    writer.write( String.valueOf( tmpGantt.get(i) ) ) ;
                }
                writer.write( "\n" ) ;
            }

        }

        // write waiting time
        writer.write( "===========================================================\n" +
                           "\n" +
                           "waiting\n" +
                           "ID\t" );

        if( fileInf.method != 6 ) {
            writer.write( methodName + "\n" );
        }
        else{
            writer.write( "FCFS\tRR\tSRTF\tPPRR\tHRRN\n" );
        }

        writer.write( "===========================================================\n" );

        for( int i = 0 ; i < processTimes.get(0).size() ; i++ ) {
            writer.write( String.valueOf( processTimes.get(0).get(i).id ) );
            for( int j = 0 ; j < processTimes.size() ; j++ ) {
                writer.write( "\t" + String.valueOf( processTimes.get(j).get(i).waitTime ) );
            }
            writer.write("\n");
        }

        // write turnaround time
        writer.write( "===========================================================\n" +
                           "\n" +
                           "Turnaround Time\n" +
                           "ID\t" );

        if( fileInf.method != 6 ) {
            writer.write( methodName + "\n" );
        }
        else{
            writer.write( "FCFS\tRR\tSRTF\tPPRR\tHRRN\n" );
        }

        writer.write( "===========================================================\n" );

        for( int i = 0 ; i < processTimes.get(0).size() ; i++ ) {
            writer.write( String.valueOf( processTimes.get(0).get(i).id ) );
            for( int j = 0 ; j < processTimes.size() ; j++ ) {
                writer.write( "\t" + String.valueOf( processTimes.get(j).get(i).turnaroundTime ) );
            }
            writer.write("\n");
        }

        if( fileInf.method == 6 ) {
            writer.write( "===========================================================\n" +
                               "\n" +
                               "" );
        }

        writer.close();
    }

    static char Convert( int id ) {
        char tmp[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
                      ,'A','B','C','D','E','F','G','H','I','J','K','L'
                      ,'M','N','O','P','Q','R','S','T','U','V','W','X'
                      ,'Y','Z'} ;
        return tmp[id];
    }

    static void testFCFS() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInformation fileInf;
        fileInf = new FileInformation();
        fileInf.Read( "input1" );

        FCFS fcfs = new FCFS();
        fcfs.FirstComeFirstServe( fileInf.processes );
        for( int i = 0 ; i < fcfs.gantt.size() ; i++ ) {
            System.out.print( fcfs.gantt.get(i) );
        }

        ArrayList< ArrayList<Character> >ganttArray = new ArrayList<>();
        ArrayList< ArrayList< ProcessTime> > processTimes = new ArrayList<>();
        ganttArray.add( fcfs.gantt);
        processTimes.add( fcfs.timings);
        Output( fileInf, ganttArray, processTimes );
    }

    static void testRR() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInformation fileInf;
        fileInf = new FileInformation();
        fileInf.Read( "input1" );

        RoundRobin rr = new RoundRobin();
        rr.doRR( fileInf.processes, fileInf.timeSlice );
        System.out.println( rr.gantt );

    }

    static void testSRTF() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInformation fileInf;
        fileInf = new FileInformation();
        fileInf.Read( "input1" );

        ShortestRemainingTimeFirst srtf = new ShortestRemainingTimeFirst();
        srtf.doSRTF( fileInf.processes );
        for( int i = 0 ; i < srtf.gantt.size() ; i++ ) {
            System.out.print( srtf.gantt.get(i) );

        }

    }

    static void testPPRR() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInformation fileInf;
        fileInf = new FileInformation();
        fileInf.Read( "input1" );

        PPRR pprr = new PPRR();
        pprr.doPPRR( fileInf.processes, fileInf.timeSlice );
        for( int i = 0 ; i < pprr.gantt.size() ; i++ ) {
            System.out.print( pprr.gantt.get(i) );

        }
        System.out.print("");

    }

    static void testHRRN() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInformation fileInf;
        fileInf = new FileInformation();
        fileInf.Read( "input1" );

        HRRN hrrn = new HRRN();
        hrrn.doHRRN( fileInf.processes );
        for( int i = 0 ; i < hrrn.gantt.size() ; i++ ) {
            System.out.print( hrrn.gantt.get(i) );
        }
        System.out.print("");
    }

    static void testALL() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInformation fileInf;
        fileInf = new FileInformation();
        fileInf.Read( "input1" );

        FCFS fcfs = new FCFS();
        fcfs.FirstComeFirstServe( fileInf.processes );

        RoundRobin rr = new RoundRobin();
        rr.doRR( fileInf.processes, fileInf.timeSlice );

        ShortestRemainingTimeFirst srtf = new ShortestRemainingTimeFirst();
        srtf.doSRTF( fileInf.processes );

        PPRR pprr = new PPRR();
        pprr.doPPRR( fileInf.processes, fileInf.timeSlice );

        HRRN hrrn = new HRRN();
        hrrn.doHRRN( fileInf.processes );

        ArrayList< ArrayList<Character> >ganttArray = new ArrayList<>();
        ArrayList< ArrayList< ProcessTime> > processTimes = new ArrayList<>();
        ganttArray.add( fcfs.gantt);
        ganttArray.add( rr.gantt);
        ganttArray.add( srtf.gantt);
        ganttArray.add( pprr.gantt);
        ganttArray.add( hrrn.gantt);
        processTimes.add( fcfs.timings);
        processTimes.add( rr.timings);
        processTimes.add( srtf.timings);
        processTimes.add( pprr.timings);
        processTimes.add( hrrn.timings);
        Output( fileInf, ganttArray, processTimes );
    }
}

class FCFS {
    ArrayList<ProcessTime> timings;
    ArrayList<Character> gantt;

    FCFS(){
        timings = new ArrayList<>();
        gantt = new ArrayList<>();
    }

    void FirstComeFirstServe( ArrayList<Process> processes ) {
        // while process terminate, find exit time
        // , then calculate turnaround time, then waiting time
        Process runningProcess;
        ArrayList<Process> copyProcesses = new ArrayList<>(processes);
        copyProcesses.sort( new FCFSComparator() );
        Queue<Process> sortedProcess = new LinkedList<>(copyProcesses);

        int time = 0;
        int exitTime;
        int waitTime;
        int turnaroundTime;
        ProcessTime processTime;

        // for each process
        for(int i = 0; !sortedProcess.isEmpty() ; i++ ) {
            runningProcess = sortedProcess.poll();

            // filling blank
            for(; time < runningProcess.arrivalTime ; time++ ) {
                gantt.add( time, '-' );
            }

            exitTime = time + runningProcess.cpuBurst;
            turnaroundTime = exitTime - runningProcess.arrivalTime;
            waitTime = turnaroundTime - runningProcess.cpuBurst;

            processTime = new ProcessTime();
            processTime.id = runningProcess.id;
            processTime.turnaroundTime = turnaroundTime;
            processTime.waitTime = waitTime;
            timings.add( processTime );

            for( ; time < exitTime ; time++ ) {
                // how to convert id to gantt letter
                gantt.add( time, G.Convert( runningProcess.id ) );
            }

        } // for each process

        timings.sort( new TimingIdComparator() );
    } // void FirstComeFirstServe

}

class RoundRobin {
    ArrayList<ProcessTime> timings;
    ArrayList<Character> gantt;

    RoundRobin(){
        timings = new ArrayList<>();
        gantt = new ArrayList<>();
    }

    void doRR( ArrayList<Process> processes, int timeSlice ){
        // sort by arrival time
        ArrayList<Process> copyProcesses = new ArrayList<>(processes);
        copyProcesses.sort( new FCFSComparator() );
        Queue<Process> sortedProcess = new LinkedList<>(copyProcesses);

        Queue<RunningProcess> readyQ = new LinkedList<>();
        int time = 0;
        int timeSliceLeft = 0 ;

        int exitTime;
        int waitTime;
        int turnaroundTime;
        ProcessTime processTime;

        RunningProcess runningProcess = null;
        RunningProcess tmp = new RunningProcess();


        for( ; !sortedProcess.isEmpty() || !readyQ.isEmpty() || runningProcess != null ; time++ ) {
            // when arrival, add to ready queue
            while( !sortedProcess.isEmpty() && time == sortedProcess.peek().arrivalTime ) {
                tmp = new RunningProcess();
                tmp.process = sortedProcess.poll();
                tmp.remainTime = tmp.process.cpuBurst;
                readyQ.offer( tmp );
            }

            // terminate or time out
            if( runningProcess != null ) {
                if( runningProcess.remainTime == 0 ) {
                    exitTime = time ;
                    turnaroundTime = exitTime - runningProcess.process.arrivalTime;
                    waitTime = turnaroundTime - runningProcess.process.cpuBurst;

                    processTime = new ProcessTime();
                    processTime.id = runningProcess.process.id;
                    processTime.turnaroundTime = turnaroundTime;
                    processTime.waitTime = waitTime;
                    timings.add( processTime );

                    runningProcess = null;

                }
                else if( timeSliceLeft == 0 ) {
                    readyQ.offer( runningProcess );
                    runningProcess = null;
                }
            }

            if( runningProcess == null && ( !readyQ.isEmpty() || !sortedProcess.isEmpty() ) ) {
                if ( readyQ.isEmpty() ) {
                    gantt.add( time, '-' );
                }
                else{
                    runningProcess = readyQ.poll();
                    timeSliceLeft = timeSlice;
                }
            }

            if( runningProcess != null ) {
                gantt.add( time, G.Convert( runningProcess.process.id ) );
                runningProcess.remainTime--;
                timeSliceLeft--;
            }

        }

        timings.sort( new TimingIdComparator() );
    }
}

class ShortestRemainingTimeFirst {
    ArrayList<ProcessTime> timings;
    ArrayList<Character> gantt;

    ShortestRemainingTimeFirst(){
        timings = new ArrayList<>();
        gantt = new ArrayList<>();
    }

    void doSRTF( ArrayList<Process> processes ){
        // sort by arrival time
        ArrayList<Process> copyProcesses = new ArrayList<>(processes);
        copyProcesses.sort( new FCFSComparator() );
        Queue<Process> sortedProcess = new LinkedList<>(copyProcesses);


        PriorityQueue<RunningProcess> readyQ = new PriorityQueue<>( sortedProcess.size(), new RemainTimeComparator() );
        int time = 0;

        int exitTime;
        int waitTime;
        int turnaroundTime;
        ProcessTime processTime;

        RunningProcess runningProcess = null;
        RunningProcess tmp;
        boolean hasNewP = false;

        for( ; !sortedProcess.isEmpty() || !readyQ.isEmpty() || runningProcess != null ; time++ ) {
            // when arrival, add to ready queue
            while( !sortedProcess.isEmpty() && time == sortedProcess.peek().arrivalTime ) {
                tmp = new RunningProcess();
                tmp.process = sortedProcess.poll();
                tmp.remainTime = tmp.process.cpuBurst;
                readyQ.offer( tmp );
                hasNewP = true;
            }

            if( runningProcess != null ) {
                // terminate
                if( runningProcess.remainTime == 0 ) {
                    exitTime = time ;
                    turnaroundTime = exitTime - runningProcess.process.arrivalTime;
                    waitTime = turnaroundTime - runningProcess.process.cpuBurst;

                    processTime = new ProcessTime();
                    processTime.id = runningProcess.process.id;
                    processTime.turnaroundTime = turnaroundTime;
                    processTime.waitTime = waitTime;
                    timings.add( processTime );

                    runningProcess = null;

                }

            }

            if( runningProcess == null && ( !readyQ.isEmpty() || !sortedProcess.isEmpty() ) ) {
                if ( readyQ.isEmpty() ) {
                    gantt.add( time, '-' );
                }
                else{
                    runningProcess = readyQ.poll();
                }
            }

            if( runningProcess != null ) {
                if( hasNewP ) {
                    readyQ.offer( runningProcess );
                    runningProcess = readyQ.poll();
                    hasNewP = false ;
                }

                gantt.add( time, G.Convert( runningProcess.process.id ) );
                runningProcess.remainTime--;
            }

        }

        timings.sort( new TimingIdComparator() );
    }
}

class PPRR {
    ArrayList<ProcessTime> timings;
    ArrayList<Character> gantt;

    PPRR(){
        timings = new ArrayList<>();
        gantt = new ArrayList<>();
    }

    void doPPRR( ArrayList<Process> processes, int timeSlice ){
        // sort by arrival time
        ArrayList<Process> copyProcesses = new ArrayList<>(processes);
        copyProcesses.sort( new FCFSComparator() );
        Queue<Process> sortedProcess = new LinkedList<>(copyProcesses);

        LinkedList<RunningProcess> readyQ = new LinkedList<>();
//        PriorityQueue<RunningProcess> readyQ = new PriorityQueue<>( sortedProcess.size(), new PriorityComparator() );
        int time = 0;

        int exitTime;
        int waitTime;
        int turnaroundTime;
        ProcessTime processTime;

        RunningProcess runningProcess = null;
        RunningProcess tmp;
        boolean hasNewP = false;

        int timeSliceLeft = 0;
        boolean doRR = false;

        for( ; !sortedProcess.isEmpty() || !readyQ.isEmpty() || runningProcess != null ; time++ ) {
            // when arrival, add to ready queue
            while( !sortedProcess.isEmpty() && time == sortedProcess.peek().arrivalTime ) {
                tmp = new RunningProcess();
                tmp.process = sortedProcess.poll();
                tmp.remainTime = tmp.process.cpuBurst;
                readyQ.offer( tmp );
                hasNewP = true;
            }
            if( hasNewP ) {
                readyQ.sort( new PriorityComparator() );
            }


            if( runningProcess != null ) {
                if( runningProcess.remainTime == 0 ) { // terminate
                    exitTime = time ;
                    turnaroundTime = exitTime - runningProcess.process.arrivalTime;
                    waitTime = turnaroundTime - runningProcess.process.cpuBurst;

                    processTime = new ProcessTime();
                    processTime.id = runningProcess.process.id;
                    processTime.turnaroundTime = turnaroundTime;
                    processTime.waitTime = waitTime;
                    timings.add( processTime );

                    runningProcess = null;
                    doRR = false;

                }
                else if( doRR && timeSliceLeft == 0 ) { // timeout
                    readyQ.offer( runningProcess );
                    readyQ.sort( new PriorityComparator() );
                    runningProcess = null;
                    doRR = false;

                }

            }

            if( runningProcess == null && ( !readyQ.isEmpty() || !sortedProcess.isEmpty() ) ) {
                if ( readyQ.isEmpty() ) {
                    gantt.add( time, '-' );
                }
                else{
                    runningProcess = readyQ.poll();
                    hasNewP = false;
                    timeSliceLeft = timeSlice;
                }
            }

            if( runningProcess != null ) {
                if( hasNewP ) { // preempt
                    if( runningProcess.process.priority != readyQ.peek().process.priority ) {
                        readyQ.offer( runningProcess );
                        readyQ.sort( new PriorityComparator() );
                        runningProcess = readyQ.poll();
                    }

                    hasNewP = false ;
                }

                if( !doRR && !readyQ.isEmpty() && runningProcess.process.priority == readyQ.peek().process.priority ) {
                    doRR = true;
//                    timeSliceLeft = timeSlice;
                }
                else if( doRR && !readyQ.isEmpty() && runningProcess.process.priority != readyQ.peek().process.priority ){
                    doRR = false;
                }

                gantt.add( time, G.Convert( runningProcess.process.id ) );
                runningProcess.remainTime--;

                timeSliceLeft--;

            }

        } // for
        timings.sort( new TimingIdComparator() );
    } // doPPRR

} // class PPRR

class HRRN {
    ArrayList<ProcessTime> timings;
    ArrayList<Character> gantt;

    HRRN(){
        timings = new ArrayList<>();
        gantt = new ArrayList<>();
    }

    void doHRRN( ArrayList<Process> processes ){
        // sort by arrival time
        ArrayList<Process> copyProcesses = new ArrayList<>(processes);
        copyProcesses.sort( new FCFSComparator() );
        Queue<Process> sortedProcess = new LinkedList<>(copyProcesses);

        PriorityQueue<RunningProcess> readyQ = new PriorityQueue<>( sortedProcess.size(), new ResponseRatioComparator() );
        int time = 0;

        int exitTime;
        int waitTime;
        int turnaroundTime;
        ProcessTime processTime;

        RunningProcess runningProcess = null;
        RunningProcess tmp;
        boolean hasNewP = false;

        for( ; !sortedProcess.isEmpty() || !readyQ.isEmpty() || runningProcess != null ; time++ ) {
            // when arrival, add to ready queue
            while( !sortedProcess.isEmpty() && time == sortedProcess.peek().arrivalTime ) {
                tmp = new RunningProcess();
                tmp.process = sortedProcess.poll();
                tmp.remainTime = tmp.process.cpuBurst;
                readyQ.offer( tmp );
                hasNewP = true;
            }

            if( runningProcess != null ) {
                // terminate
                if( runningProcess.remainTime == 0 ) {
                    exitTime = time ;
                    turnaroundTime = exitTime - runningProcess.process.arrivalTime;
                    waitTime = turnaroundTime - runningProcess.process.cpuBurst;

                    processTime = new ProcessTime();
                    processTime.id = runningProcess.process.id;
                    processTime.turnaroundTime = turnaroundTime;
                    processTime.waitTime = waitTime;
                    timings.add( processTime );

                    runningProcess = null;

                }
            }

            if( runningProcess == null && ( !readyQ.isEmpty() || !sortedProcess.isEmpty() ) ) {
                if ( readyQ.isEmpty() ) {
                    gantt.add( time, '-' );
                }
                else{
                    runningProcess = readyQ.poll();
                }
            }

            if( runningProcess != null ) {
                // non preemptive
//                readyQ.offer( runningProcess );
//                runningProcess = readyQ.poll();
//                hasNewP = false ;

                gantt.add( time, G.Convert( runningProcess.process.id ) );
                runningProcess.remainTime--;

                ArrayList<RunningProcess> readyQTmp = new ArrayList<>( readyQ );
                readyQ.clear();
                for( int i = 0 ; i < readyQTmp.size() ; i++ ) {
                    RunningProcess tmpRP = readyQTmp.get(i);
                    tmpRP.waitTime++;
                    readyQ.offer( tmpRP );
                }


            } // if runningProcess != null

        } // for

        timings.sort( new TimingIdComparator() );
    }
}

class RunningProcess{
    public Process process ;
    public int remainTime ;
    public double waitTime;

    RunningProcess(){
        process = new Process();
        remainTime = -1;
        waitTime = 0.0;
    }
}

class ProcessTime {
    public int id;
    public int waitTime;
    public int turnaroundTime;
}

class FCFSComparator implements Comparator<Process> {
    public int compare(Process t1, Process t2) {
        if( t1.arrivalTime < t2.arrivalTime ) {
            return -1 ;
        }
        else if ( t1.arrivalTime > t2.arrivalTime ) {
            return 1;
        }
        else if ( t1.id < t2.id ) {
            return -1;
        }
        else if( t1.id > t2.id ) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class TimingIdComparator implements Comparator<ProcessTime> {
    public int compare( ProcessTime p1, ProcessTime p2) {
        if ( p1.id < p2.id ) {
            return -1;
        }
        else if ( p1.id > p2.id ) {
            return 1 ;
        }
        else {
            return 0;
        }
    }
}

class RemainTimeComparator implements Comparator<RunningProcess> {
    public int compare( RunningProcess p1, RunningProcess p2) {
        if( p1.remainTime < p2.remainTime ) {
            return -1 ;
        }
        else if ( p1.remainTime > p2.remainTime ) {
            return 1;
        }
        else if ( p1.process.arrivalTime < p2.process.arrivalTime ) {
            return -1;
        }
        else if( p1.process.arrivalTime > p2.process.arrivalTime ) {
            return 1;
        }
        else if ( p1.process.id < p2.process.id ) {
            return -1;
        }
        else if( p1.process.id > p2.process.id ) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class PriorityComparator implements Comparator<RunningProcess> {
    public int compare( RunningProcess p1, RunningProcess p2) {
        if( p1.process.priority < p2.process.priority ) {
            return -1 ;
        }
        else if ( p1.process.priority > p2.process.priority ) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class ResponseRatioComparator implements Comparator<RunningProcess> {
    public int compare( RunningProcess p1, RunningProcess p2) {
        double responseRatio1 =  ( p1.waitTime + p1.process.cpuBurst ) / p1.process.cpuBurst ;
        double responseRatio2 =  ( p2.waitTime + p2.process.cpuBurst ) / p2.process.cpuBurst ;

        if( responseRatio1 > responseRatio2 ) {
            return -1 ;
        }
        else if ( responseRatio1 < responseRatio2 ) {
            return 1;
        }
        else if ( p1.process.arrivalTime < p2.process.arrivalTime ) {
            return -1;
        }
        else if( p1.process.arrivalTime > p2.process.arrivalTime ) {
            return 1;
        }
        else if ( p1.process.id < p2.process.id ) {
            return -1;
        }
        else if( p1.process.id > p2.process.id ) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class FileInformation {
    public String fileName;
    public int method;
    public int timeSlice;
    public ArrayList<Process> processes;

    FileInformation(){
        fileName = "";
        method = 0;
        timeSlice = 0;
        processes = new ArrayList<>();
    }

    boolean Read( String fileName ) throws Throwable {
        if( !fileName.endsWith( ".txt" ) ) {
            fileName = fileName + ".txt";
        }

        File file = new File( fileName ) ;
        if ( !file.exists() ) {
            return false ;
        }
        else {
            this.fileName = fileName;
            Scanner scanner = new Scanner( file );
            method = scanner.nextInt();
            timeSlice = scanner.nextInt();
            scanner.nextLine(); // remove /n
            scanner.nextLine(); // remove the column

            Process process;
            while ( scanner.hasNextInt()  ) {
                process = new Process();
                process.id = scanner.nextInt();
                process.cpuBurst = scanner.nextInt();
                process.arrivalTime = scanner.nextInt();
                process.priority = scanner.nextInt();
                processes.add(process);

//                System.out.print( process.id + ", " );
//                System.out.print( process.cpuBurst + ", " );
//                System.out.print( process.arrivalTime + ", " );
//                System.out.println( process.priority );
            }
            return true ;
        }
    }

}

class Process {
    public int id;
    public int cpuBurst;
    public int arrivalTime;
    public int priority;

    Process(){
        id = 0;
        cpuBurst = 0;
        arrivalTime = 0;
        priority = 0;
    }

}


public class Main {
    public static void main( String[] args ) throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInformation fileInf;
        boolean readFileSuccess;
        boolean keepRun = true;
        ArrayList< ArrayList<Character> > ganttArray ;
        ArrayList< ArrayList< ProcessTime> > processTimes;

        while ( keepRun ) {
            fileInf = new FileInformation();
            readFileSuccess = false;

            while( !readFileSuccess ) {
                System.out.print( "檔名: " );
                readFileSuccess = fileInf.Read( scanner.next() );
                if( !readFileSuccess ) {
                    System.out.println( "找不到檔案" );
                }
            }

            ganttArray = new ArrayList<>();
            processTimes = new ArrayList<>();

            if( fileInf.method == 1 || fileInf.method == 6 ) {
                FCFS fcfs = new FCFS();
                fcfs.FirstComeFirstServe( fileInf.processes );
                ganttArray.add( fcfs.gantt);
                processTimes.add( fcfs.timings);
            }

            if ( fileInf.method == 2 || fileInf.method == 6 ) {
                RoundRobin rr = new RoundRobin();
                rr.doRR( fileInf.processes, fileInf.timeSlice );
                ganttArray.add( rr.gantt);
                processTimes.add( rr.timings);
            }


            if ( fileInf.method == 3 || fileInf.method == 6 ) {
                ShortestRemainingTimeFirst srtf = new ShortestRemainingTimeFirst();
                srtf.doSRTF( fileInf.processes );
                ganttArray.add( srtf.gantt);
                processTimes.add( srtf.timings);
            }

            if ( fileInf.method == 4 || fileInf.method == 6 ) {
                PPRR pprr = new PPRR();
                pprr.doPPRR( fileInf.processes, fileInf.timeSlice );
                ganttArray.add( pprr.gantt);
                processTimes.add( pprr.timings);
            }

            if ( fileInf.method == 5 || fileInf.method == 6 ) {
                HRRN hrrn = new HRRN();
                hrrn.doHRRN( fileInf.processes );
                ganttArray.add( hrrn.gantt);
                processTimes.add( hrrn.timings);
            }

            G.Output( fileInf, ganttArray, processTimes );

            System.out.print( "done\n" +
                              "還有檔案要跑嗎?(y/n): " );
            if( scanner.next().charAt(0) == 'n' ) {
                keepRun = false ;
            }
        }

    } // main

}

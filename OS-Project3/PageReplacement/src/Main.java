import java.awt.*;
import java.io.*;
import java.util.*;

/*
input: page frame, page reference string
operate: 各種page replacement method
output: 記錄每次page reference, page frame內容 & page fault, page replace次數
*/

class G {
    static void testFIFO() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInf fileInf = new FileInf();
        fileInf.Read("input_testFIFO");
        ArrayList<OutputRecord> records = new ArrayList<>();
        records.add(runFIFO( fileInf ));
        G.Output(fileInf, records );
    }

    static void testLRU() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInf fileInf = new FileInf();
        fileInf.Read("input_testLRU");
        G.runLRU( fileInf );
    }

    static void testLFUFIFO() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInf fileInf = new FileInf();
        fileInf.Read("input_testLRU");
        G.runLFUFIFO(fileInf);
    }

    static void testMFUFIFO() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInf fileInf = new FileInf();
        fileInf.Read("input_testLRU");
        G.runMFUFIFO(fileInf);
    }

    static void testLFRU() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInf fileInf = new FileInf();
        fileInf.Read("input_testLRU");
        G.runLFRU(fileInf);
    }

    static void testMFRU() throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInf fileInf = new FileInf();
        fileInf.Read("input_testLRU");
        G.runMFRU(fileInf);
    }

    static OutputRecord runFIFO( FileInf fileInf ){
        PriorityQueue<TimestampFrame> frames = new PriorityQueue<>(3, new TimestampComparator());
        TimestampFrame tmpTimestampFrame;
        char curPage ;
        String refList = fileInf.pageRef;
        int numPageFault = 0;
        int numPageReplace = 0;
        boolean frameExist ;
        ArrayList<TimestampFrame> tmpFrames;
        FrameRecord frameRecord ;
        OutputRecord outputRecord = new OutputRecord();

        for( int i = 0 ; i < refList.length() ; i++){
            curPage = refList.charAt(i);
            frameExist = false;
            tmpFrames = new ArrayList<>();
            frameRecord = new FrameRecord();

            while( !frames.isEmpty() ) {
                tmpTimestampFrame = frames.poll();
                if( tmpTimestampFrame.num == curPage ) {
                    frameExist = true;
                }
                tmpFrames.add(tmpTimestampFrame);
            }
            while( !tmpFrames.isEmpty() ) {
                frames.offer( tmpFrames.remove(0));
            }

            if(!frameExist) {
                numPageFault++;
                frameRecord.fault = true;
                if( frames.size() == fileInf.numFrame ) {
                    frames.poll();
                    numPageReplace++;
                }

                tmpTimestampFrame = new TimestampFrame();
                tmpTimestampFrame.num = curPage;
                tmpTimestampFrame.timestamp = System.nanoTime();
                frames.offer(tmpTimestampFrame);
            }

            // priority queue 只有取出來時會是正確的排序，要正確地顯示只好另外sort
            tmpFrames = new ArrayList<>( frames );
            Collections.sort(tmpFrames, new TimestampComparator());
            for( int j = 0 ; j < tmpFrames.size() ; j++) {
                frameRecord.frames.add( tmpFrames.get(j).num ) ;
            }
            outputRecord.frameRecords.add( frameRecord );
        } // for reference list

        outputRecord.numPageFault = numPageFault;
        outputRecord.numPageReplace = numPageReplace;
        return outputRecord;
    } // runFIFO

    static OutputRecord runLRU( FileInf fileInf ){
        PriorityQueue<TimestampFrame> frames = new PriorityQueue<>(3, new TimestampComparator());
        TimestampFrame tmpTimestampFrame;
        char curPage ;
        String refList = fileInf.pageRef;
        int numPageFault = 0;
        int numPageReplace = 0;
        boolean frameExist ;
        ArrayList<TimestampFrame> tmpFrames;
        FrameRecord frameRecord ;
        OutputRecord outputRecord = new OutputRecord();

        for( int i = 0 ; i < refList.length() ; i++){
            curPage = refList.charAt(i);
            frameExist = false;
            tmpFrames = new ArrayList<>();
            frameRecord = new FrameRecord();

            while( !frames.isEmpty() ) {
                tmpTimestampFrame = frames.poll();
                if( tmpTimestampFrame.num == curPage ) {
                    tmpTimestampFrame.timestamp = System.nanoTime();
                    frameExist = true;
                }
                tmpFrames.add(tmpTimestampFrame);
            }
            while( !tmpFrames.isEmpty() ) {
                frames.offer( tmpFrames.remove(0));
            }

            if(!frameExist) {
                numPageFault++;
                frameRecord.fault = true;
                if( frames.size() == fileInf.numFrame ) {
                    frames.poll();
                    numPageReplace++;
                }

                tmpTimestampFrame = new TimestampFrame();
                tmpTimestampFrame.num = curPage;
                tmpTimestampFrame.timestamp = System.nanoTime();
                frames.offer(tmpTimestampFrame);
            }

            tmpFrames = new ArrayList<>( frames );
            Collections.sort(tmpFrames, new TimestampComparator());
            for( int j = 0 ; j < tmpFrames.size() ; j++) {
                frameRecord.frames.add( tmpFrames.get(j).num ) ;
            }
            outputRecord.frameRecords.add( frameRecord );
        } // for reference list


        outputRecord.numPageFault = numPageFault;
        outputRecord.numPageReplace = numPageReplace;
        return outputRecord;
    }

    static OutputRecord runLFUFIFO( FileInf fileInf ) {
        PriorityQueue<CounterTimestampFrame> frames = new PriorityQueue<>(3, new LFUComparator());
        CounterTimestampFrame tmpFrame;
        char curPage ;
        String refList = fileInf.pageRef;
        int numPageFault = 0;
        int numPageReplace = 0;
        boolean frameExist ;
        ArrayList<CounterTimestampFrame> tmpFrames;
        FrameRecord frameRecord ;
        OutputRecord outputRecord = new OutputRecord();

        for( int i = 0 ; i < refList.length() ; i++){
            curPage = refList.charAt(i);
            frameExist = false;
            tmpFrames = new ArrayList<>();
            frameRecord = new FrameRecord();

            while( !frames.isEmpty() ) {
                tmpFrame = frames.poll();
                if( tmpFrame.num == curPage ) {
                    frameExist = true;
                    tmpFrame.counter++;
                }
                tmpFrames.add(tmpFrame);
            }
            while( !tmpFrames.isEmpty() ) {
                frames.offer( tmpFrames.remove(0));
            }

            if(!frameExist) {
                numPageFault++;
                frameRecord.fault = true;
                if( frames.size() == fileInf.numFrame ) {
                    frames.poll();
                    numPageReplace++;
                }

                tmpFrame = new CounterTimestampFrame();
                tmpFrame.num = curPage;
                tmpFrame.timestamp = System.nanoTime();
                frames.offer(tmpFrame);
            }

            tmpFrames = new ArrayList<>( frames );
            Collections.sort(tmpFrames, new NoCounterTimestampComparator() );
            for( int j = 0 ; j < tmpFrames.size() ; j++) {
                frameRecord.frames.add( tmpFrames.get(j).num ) ;
            }
            outputRecord.frameRecords.add( frameRecord );
        } // for reference list
        outputRecord.numPageFault = numPageFault;
        outputRecord.numPageReplace = numPageReplace;
        return outputRecord;
    }

    static OutputRecord runMFUFIFO( FileInf fileInf ) {
        PriorityQueue<CounterTimestampFrame> frames = new PriorityQueue<>(3, new MFUComparator());
        CounterTimestampFrame tmpFrame;
        char curPage ;
        String refList = fileInf.pageRef;
        int numPageFault = 0;
        int numPageReplace = 0;
        boolean frameExist ;
        ArrayList<CounterTimestampFrame> tmpFrames;
        FrameRecord frameRecord ;
        OutputRecord outputRecord = new OutputRecord();

        for( int i = 0 ; i < refList.length() ; i++){
            curPage = refList.charAt(i);
            frameExist = false;
            tmpFrames = new ArrayList<>();
            frameRecord = new FrameRecord();

            while( !frames.isEmpty() ) {
                tmpFrame = frames.poll();
                if( tmpFrame.num == curPage ) {
                    frameExist = true;
                    tmpFrame.counter++;
                }
                tmpFrames.add(tmpFrame);
            }
            while( !tmpFrames.isEmpty() ) {
                frames.offer( tmpFrames.remove(0));
            }

            if(!frameExist) {
                numPageFault++;
                frameRecord.fault = true;
                if( frames.size() == fileInf.numFrame ) {
                    frames.poll();
                    numPageReplace++;
                }

                tmpFrame = new CounterTimestampFrame();
                tmpFrame.num = curPage;
                tmpFrame.timestamp = System.nanoTime();
                frames.offer(tmpFrame);
            }

            tmpFrames = new ArrayList<>( frames );
            Collections.sort(tmpFrames, new NoCounterTimestampComparator() );
            for( int j = 0 ; j < tmpFrames.size() ; j++) {
                frameRecord.frames.add( tmpFrames.get(j).num ) ;
            }
            outputRecord.frameRecords.add( frameRecord );
        } // for reference list
        outputRecord.numPageFault = numPageFault;
        outputRecord.numPageReplace = numPageReplace;
        return outputRecord;
    }

    static OutputRecord runLFRU( FileInf fileInf ){
        PriorityQueue<CounterTimestampFrame> frames = new PriorityQueue<>(3, new LFUComparator());
        CounterTimestampFrame tmpFrame;
        char curPage ;
        String refList = fileInf.pageRef;
        int numPageFault = 0;
        int numPageReplace = 0;
        boolean frameExist ;
        ArrayList<CounterTimestampFrame> tmpFrames;
        FrameRecord frameRecord ;
        OutputRecord outputRecord = new OutputRecord();

        for( int i = 0 ; i < refList.length() ; i++){
            curPage = refList.charAt(i);
            frameExist = false;
            tmpFrames = new ArrayList<>();
            frameRecord = new FrameRecord();

            while( !frames.isEmpty() ) {
                tmpFrame = frames.poll();
                if( tmpFrame.num == curPage ) {
                    tmpFrame.timestamp = System.nanoTime();
                    tmpFrame.counter++;
                    frameExist = true;
                }
                tmpFrames.add(tmpFrame);
            }
            while( !tmpFrames.isEmpty() ) {
                frames.offer( tmpFrames.remove(0));
            }

            if(!frameExist) {
                numPageFault++;
                frameRecord.fault = true;
                if( frames.size() == fileInf.numFrame ) {
                    frames.poll();
                    numPageReplace++;
                }

                tmpFrame = new CounterTimestampFrame();
                tmpFrame.num = curPage;
                tmpFrame.timestamp = System.nanoTime();
                frames.offer(tmpFrame);
            }

            tmpFrames = new ArrayList<>( frames );
            Collections.sort(tmpFrames, new NoCounterTimestampComparator());
            for( int j = 0 ; j < tmpFrames.size() ; j++) {
                frameRecord.frames.add( tmpFrames.get(j).num ) ;
            }
            outputRecord.frameRecords.add( frameRecord );
        } // for reference list
        outputRecord.numPageFault = numPageFault;
        outputRecord.numPageReplace = numPageReplace;
        return outputRecord;
    }

    static OutputRecord runMFRU( FileInf fileInf ) {
        PriorityQueue<CounterTimestampFrame> frames = new PriorityQueue<>(3, new MFUComparator());
        CounterTimestampFrame tmpFrame;
        char curPage ;
        String refList = fileInf.pageRef;
        int numPageFault = 0;
        int numPageReplace = 0;
        boolean frameExist ;
        ArrayList<CounterTimestampFrame> tmpFrames;
        FrameRecord frameRecord ;
        OutputRecord outputRecord = new OutputRecord();

        for( int i = 0 ; i < refList.length() ; i++){
            curPage = refList.charAt(i);
            frameExist = false;
            tmpFrames = new ArrayList<>();
            frameRecord = new FrameRecord();

            while( !frames.isEmpty() ) {
                tmpFrame = frames.poll();
                if( tmpFrame.num == curPage ) {
                    frameExist = true;
                    tmpFrame.timestamp = System.nanoTime();
                    tmpFrame.counter++;
                }
                tmpFrames.add(tmpFrame);
            }
            while( !tmpFrames.isEmpty() ) {
                frames.offer( tmpFrames.remove(0));
            }

            if(!frameExist) {
                numPageFault++;
                frameRecord.fault = true;
                if( frames.size() == fileInf.numFrame ) {
                    frames.poll();
                    numPageReplace++;
                }

                tmpFrame = new CounterTimestampFrame();
                tmpFrame.num = curPage;
                tmpFrame.timestamp = System.nanoTime();
                frames.offer(tmpFrame);
            }

            tmpFrames = new ArrayList<>( frames );
            Collections.sort(tmpFrames, new NoCounterTimestampComparator());
            for( int j = 0 ; j < tmpFrames.size() ; j++) {
                frameRecord.frames.add( tmpFrames.get(j).num ) ;
            }
            outputRecord.frameRecords.add( frameRecord );
        } // for reference list
        outputRecord.numPageFault = numPageFault;
        outputRecord.numPageReplace = numPageReplace;
        return outputRecord;
    }

    static void Output( FileInf fileInf, ArrayList<OutputRecord> records ) throws Throwable {
        fileInf.fileName = fileInf.fileName.replace( ".txt", "" );
        fileInf.fileName = "out_" + fileInf.fileName + ".txt" ;
        FileWriter fileWriter = new FileWriter( fileInf.fileName ) ;
        BufferedWriter writer = new BufferedWriter( fileWriter ) ;

        OutputRecord tmpRecord ;
        ArrayList<Character> frames ;
        for( int k = 0 ; k < records.size() ; k++ ) {
            if( k == 0 ) {
                writer.write( "--------------FIFO-----------------------\n" ) ;
            }
            else if( k == 1 ) {
                writer.write( "\n--------------LRU-----------------------\n" ) ;
            }
            else if( k == 2 ) {
                writer.write( "\n--------------Least Frequently Used Page Replacement-----------------------\n" ) ;
            }
            else if( k == 3 ) {
                writer.write( "\n--------------Most Frequently Used Page Replacement -----------------------\n" ) ;
            }
            else if( k == 4 ) {
                writer.write( "\n--------------Least Frequently Used LRU Page Replacement-----------------------\n" ) ;
            }
            else if( k == 5 ) {
                writer.write( "\n--------------Most Frequently Used LRU Page Replacement -----------------------\n" ) ;
            }

            tmpRecord = records.get(k);
            for( int i = 0 ; i < tmpRecord.frameRecords.size() ; i++ ) {
                writer.write( String.valueOf( fileInf.pageRef.charAt(i) ) + "\t" ); // cur reference page
                frames = tmpRecord.frameRecords.get(i).frames;
                Collections.reverse( frames );
                for ( int j = 0 ; j < frames.size() ; j++ ) {
                    writer.write( frames.get(j) );
                }

                if( tmpRecord.frameRecords.get(i).fault ) {
                    writer.write( "\tF" );
                }
                writer.write( "\n");
            }
            writer.write( "Page Fault = " + tmpRecord.numPageFault
                         + "  Page Replaces = " + tmpRecord.numPageReplace
                         + "  Page Frames = " + fileInf.numFrame + "\n" );
        } // for all records
        writer.close();
    } // void Output

} // class G

class FrameRecord{
    ArrayList<Character> frames;
    boolean fault;

    FrameRecord(){
        frames = new ArrayList<>();
        fault = false;
    }
}

class OutputRecord{
    ArrayList<FrameRecord> frameRecords;
    int numPageFault;
    int numPageReplace;

    OutputRecord(){
        frameRecords = new ArrayList<>();
        numPageReplace = 0;
        numPageFault = 0 ;
    }
}

class FileInf {
    String fileName;
    int numFrame; // number of frame
    String pageRef; // page reference string

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
            numFrame = scanner.nextInt();
            pageRef = scanner.next(); // might have trouble

            return true ;
        }
    }

}

class TimestampComparator implements Comparator<TimestampFrame> {
    public int compare(TimestampFrame f1, TimestampFrame f2) {
        if( f1.timestamp < f2.timestamp )  {
            return -1 ;
        }
        else if ( f1.timestamp > f2.timestamp ) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class NoCounterTimestampComparator  implements  Comparator<CounterTimestampFrame> {
    public int compare( CounterTimestampFrame f1, CounterTimestampFrame f2) {
        if( f1.timestamp < f2.timestamp )  {
            return -1 ;
        }
        else if ( f1.timestamp > f2.timestamp ) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class LFUComparator implements Comparator<CounterTimestampFrame> {
    public int compare( CounterTimestampFrame f1, CounterTimestampFrame f2) {
        if ( f1.counter < f2.counter ) {
            return -1;
        }
        else if( f1.counter > f2.counter ) {
            return 1;
        }
        else if( f1.timestamp < f2.timestamp )  {
            return -1 ;
        }
        else if ( f1.timestamp > f2.timestamp ) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class MFUComparator implements Comparator<CounterTimestampFrame> {
    public int compare( CounterTimestampFrame f1, CounterTimestampFrame f2) {
        if ( f1.counter > f2.counter ) {
            return -1;
        }
        else if( f1.counter < f2.counter ) {
            return 1;
        }
        else if( f1.timestamp < f2.timestamp )  {
            return -1 ;
        }
        else if ( f1.timestamp > f2.timestamp ) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class TimestampFrame {
    public char num;
    public long timestamp;

    TimestampFrame(){
        num = '\0';
        timestamp = 0;
    }
}

class CounterTimestampFrame {
    public char num;
    public long timestamp;
    public int counter;

    CounterTimestampFrame(){
        num = '\0';
        timestamp = 0;
        counter = 0;
    }
}

public class Main {
    public static void main( String[] args ) throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        FileInf fileInf ;
        boolean readFileSuccess;
        boolean keepRun = true;
        ArrayList<OutputRecord> outputRecords;

        while ( keepRun ) {
            fileInf = new FileInf();
            readFileSuccess = false;

            while( !readFileSuccess ) {
                System.out.print( "檔名: " );
                readFileSuccess = fileInf.Read( scanner.next() );
                if( !readFileSuccess ) {
                    System.out.println( "找不到檔案" );
                }
            }

            outputRecords = new ArrayList<>();

            outputRecords.add( G.runFIFO( fileInf ) );
            outputRecords.add( G.runLRU( fileInf ) );
            outputRecords.add( G.runLFUFIFO( fileInf ) );
            outputRecords.add( G.runMFUFIFO( fileInf ) );
            outputRecords.add( G.runLFRU( fileInf ) );
            outputRecords.add( G.runMFRU( fileInf ) );

            G.Output( fileInf, outputRecords );

            System.out.print( "done\n" +
                    "還有檔案要跑嗎?(y/n): " );
            if( scanner.next().charAt(0) == 'n' ) {
                keepRun = false ;
            }
        }
    }

} // main

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.ZonedDateTime;
import java.util.*;
import java.io.File;

class G {
    static boolean ReadFile( String fileName, ArrayList<Integer> list ) throws Throwable  {
        if( !fileName.endsWith( ".txt" ) ) {
            fileName = fileName + ".txt";
        }

        File file = new File( fileName ) ;
        if ( !file.exists() ) {
            return false ;
        }
        else {
           Scanner scanner = new Scanner(file);
           while ( scanner.hasNext() ) {
               list.add( scanner.nextInt() );
           }
           return true ;
        }
    }

    static void TestReadFile() throws Throwable {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if( ReadFile("input_1", list ) ) {
            System.out.println("read file success");
            System.out.println(list);
        }
        else {
            System.out.print("read file fail");
        }

    }

    static long MergeBubbleSortTime( ArrayList<Integer> input, float k ) {
        int subListSize = (int)Math.ceil( input.size() / k ) ;
        long startTime = System.nanoTime();


        int start = 0, end = subListSize - 1;
        while ( end < input.size() ) {
            BubbleSort.BubbleSort( input, start, end );

            start = end + 1 ;
            end = end + subListSize;
        }
        if( start < input.size() ) {
            BubbleSort.BubbleSort( input, start, input.size() - 1 );
        }

        Merge.MergeArrays( input, subListSize );

        return System.nanoTime() - startTime;
    }

    static void TestBubbleMergeSortTime() throws Throwable {
        ArrayList<Integer> input = new ArrayList<Integer>();
        G.ReadFile( "input_1w", input );

        long time = MergeBubbleSortTime( input, 3);

        Output(input, time, "input_1w", 4);

    }

    static void PrintArray( ArrayList<Integer> input ){
        for( int i = 0 ; i < input.size() ; i++ ) {
            if (i != 0) {
                System.out.print(", ");
            }
            System.out.print(input.get(i));
        }
        System.out.println();
    }

    static void Output( ArrayList<Integer> list, long time, String inputName,
                        int methodNum ) throws Throwable {
        inputName.replace( ".txt", "" );
        String newFile = inputName + "_output" + methodNum + ".txt" ;
        FileWriter fileWriter = new FileWriter( newFile ) ;
        BufferedWriter writer = new BufferedWriter( fileWriter ) ;
        for( int i = 0 ; i < list.size() ; i++ ) {
            writer.write( String.valueOf( list.get(i) ) ) ;
            writer.write( "\n" ) ;
//            System.out.println( list.get(i) ) ;
        }

        writer.write( "CPU Time: " + time + "\n" );
//        System.out.print( "CPU Time: " + time + "\n" );

        ZonedDateTime curTime = ZonedDateTime.now();
        writer.write( "Output Time: " + String.valueOf( curTime ) );
//        System.out.println( "Output Time:" + curTime );

        writer.close();
    }

    static void TestOutput() throws Throwable {
        ArrayList<Integer> list = new ArrayList<Integer>();
        G.ReadFile( "input_1w.txt", list );
        long l = 123;
        Output( list, l, "input_1w", 5  );
    }

    static void ThreadBubbleSort( ArrayList<Integer> input, int subListSize ) throws Throwable{
        ArrayList<Thread> threads = new ArrayList<Thread>();
        Thread t;
        int start = 0, end = subListSize - 1;
        while( end < input.size() ){
            t = new Thread( new BubbleSort( input, start, end ) );
            t.start();
            threads.add( t );

            start = end + 1;
            end = end + subListSize;
        }
        if( start < input.size() ) {
            t = new Thread( new BubbleSort( input, start, input.size() - 1 ) );
            t.start();
            threads.add( t );

        }

        for( int i = 0 ; i < threads.size() ; i++){
            threads.get( i ).join();
        }
    }

    static void TestThreadBubbleSort() throws Throwable {
        ArrayList<Integer> input = new ArrayList<Integer>();
        input.add(5);
        input.add(2);
        input.add(4);
        input.add(1);
        input.add(8);
        input.add(9);
        input.add(5);
        input.add(6);
        input.add(7);

        int subListSize = (int)Math.ceil( input.size() / 5.0 );
        ThreadBubbleSort( input, subListSize );

        System.out.println( input );
    }

    static long ThreadSort( ArrayList<Integer> input, double k ) throws Throwable{
        long startTime = System.nanoTime();

        int subListSize = (int)Math.ceil( input.size() / k ) ;

        ThreadBubbleSort( input, subListSize );

        ThreadMergeArrays( input, subListSize );

        return System.nanoTime() - startTime ;
    }

    static void ThreadMergeArrays( ArrayList<Integer > input, int subListSize ) throws Throwable {
        int mergeLen = 2 * subListSize;
        int start = 0, mid = 0, end = 0 ;
        ArrayList<Thread> threads = new ArrayList<Thread>();
        Thread t ;

        while( mergeLen <= input.size() ) {
            start = 0;
            mid = start + ( mergeLen / 2 ) - 1;
            end = mergeLen - 1;
            threads = new ArrayList<Thread>();

            while ( end < input.size() ) {
                t = new Thread( new Merge( input, start, mid, end ) );
                t.start();
                threads.add( t );

                start = end + 1 ;
                mid = start + ( mergeLen / 2 ) - 1 ;
                end = end + mergeLen;
            }
            if( start < input.size() ) {
                if( mid < input.size() ) {
                    t = new Thread( new Merge( input, start, mid, input.size() - 1 ) );
                    t.start();
                    threads.add( t ) ;
                }
            }

            for( int i = 0 ; i < threads.size() ; i++){
                threads.get( i ).join();
            }

            mergeLen = 2 * mergeLen;
        } // while
        if ( ( mergeLen / 2 ) != input.size() ) {
            t = new Thread( new Merge( input, 0, ( mergeLen / 2 ) - 1, input.size() - 1 ) );
            t.start();
            t.join();
        }
    }

    static void TestThreadSort() throws Throwable{
        ArrayList<Integer> input = new ArrayList<Integer>();
        G.ReadFile( "input_1w", input );

        long time = ThreadSort( input, 3);

        Output(input, time, "input_1w", 2);
    }
}

class BubbleSort implements Runnable {
    ArrayList<Integer> input;
    int start;
    int end;

    BubbleSort( ArrayList<Integer> input, int start, int end ){
        this.input = input;
        this.start = start;
        this.end = end;
    }

    static void Swap( ArrayList<Integer> input, int indexA, int indexB ) {
        int temp = 0;
        temp = input.get( indexA );
        input.set( indexA, input.get( indexB ) );
        input.set( indexB, temp );
    }

    static void BubbleSort( ArrayList<Integer> input, int start, int end ) {
        // start & end are index
        // end is included
        for( int i = 0 ; i <= ( end - start - 1 ) ; i++ ) {
            for( int j = start ; j <= ( end - i  - 1 ) ; j++ ) {
                if( input.get(j) > input.get(j+1)) {
                    Swap( input, j, j+1 ) ;
                }
            }
        }
    }

    static void BubbleSort( ArrayList<Integer> input ) {
        BubbleSort( input, 0, input.size() - 1 );
    }

    static void TestBubbleSort() {
        ArrayList<Integer> input = new ArrayList<Integer>();
        for( int i = 10 ; i >= 0 ; i-- ) {
            input.add( i ) ;
        }

        for( int i = 0 ; i < input.size() ; i++ ) {
            if( i != 0 ) {
                System.out.print( "," ) ;
            }
            System.out.print( input.get(i) );
        }
        System.out.println();

        BubbleSort( input ) ;

        for( int i = 0 ; i < input.size() ; i++ ) {
            if (i != 0) {
                System.out.print(",");
            }
            System.out.print(input.get(i));
        }
        System.out.println();
    }

    static long BubbleSortTime( ArrayList<Integer> list ) {
        long start = System.nanoTime();
        BubbleSort( list );
        return System.nanoTime() - start;
    }

    static void TestBubbleSortTime() throws Throwable {
        ArrayList<Integer> list = new ArrayList<Integer>();
        G.ReadFile( "input_1w", list );
        System.out.println( BubbleSortTime( list ) );
    }

    public void run(){
        BubbleSort( input, start, end );
//        System.out.println( "done" );
    }
}

class Merge implements Runnable {
    ArrayList<Integer> input;
    int start, mid, end;

    Merge( ArrayList<Integer> input, int start, int mid, int end ){
        this.input = input;
        this.start = start;
        this.mid = mid;
        this.end = end;
    }

    static void Merge( ArrayList<Integer> list, int start, int mid, int end ) {
        // mid = end index of 1st array
        // end is included
        ArrayList<Integer> arr1 =new ArrayList<Integer>( list.subList( start, mid + 1 ) ) ;
        ArrayList<Integer> arr2 =new ArrayList<Integer>( list.subList( mid + 1, end + 1 ) ) ;

        int arr1CurIndex = 0, arr2CurIndex = 0;
        int listCurIndex = start ;
        while( arr1CurIndex < arr1.size() && arr2CurIndex < arr2.size() ) {
            int arr1Value = arr1.get( arr1CurIndex );
            int arr2Value = arr2.get( arr2CurIndex ) ;

            if( arr1Value <= arr2Value ) {
                list.set( listCurIndex, arr1Value );
                arr1CurIndex++;
            }
            else {
                list.set( listCurIndex, arr2Value );
                arr2CurIndex++;
            }

            listCurIndex++;
        }

        while ( arr1CurIndex < arr1.size() ) {
            list.set( listCurIndex, arr1.get( arr1CurIndex ) );
            listCurIndex++;
            arr1CurIndex++;
        }
        while ( arr2CurIndex < arr2.size() ) {
            list.set( listCurIndex, arr2.get( arr2CurIndex ) );
            listCurIndex++;
            arr2CurIndex++;
        }
    }

    static void TestMerge() {
        ArrayList<Integer> input = new ArrayList<Integer>();
        for( int i = 10 ; i > 0 ; i-- ) {
            input.add( i ) ;
        }

        Merge( input, 0, 4, input.size() - 1 );

        System.out.println( input );
    }

    static void MergeArrays( ArrayList<Integer > input, int subListSize ) {
        int mergeLen = 2 * subListSize;
        int start = 0, mid = 0, end = 0 ;

        while( mergeLen <= input.size() ) {
            start = 0;
            mid = start + ( mergeLen / 2 ) - 1;
            end = mergeLen - 1;

            while ( end < input.size() ) {
                Merge( input, start, mid, end );

                start = end + 1 ;
                mid = start + ( mergeLen / 2 ) - 1 ;
                end = end + mergeLen;
            }
            if( start < input.size() ) {
                if( mid < input.size() ) {
                    Merge( input, start, mid, input.size() - 1 );
                }
            }

            mergeLen = 2 * mergeLen;
        } // while
        if ( ( mergeLen / 2 ) != input.size() ) {
            Merge( input, 0, ( mergeLen / 2 ) - 1, input.size() - 1 );
        }
    }

    static void TestMergeArrays(){

    }

    public void run(){
        Merge( input, start, mid, end);
    }
}

public class Main {
    public static void main( String[] args) throws Throwable {
        Scanner scanner = new Scanner( System.in ) ;
        boolean readFileSuccess;
        String fileName;
        ArrayList<Integer> input;

        while( true ) {
            readFileSuccess = false ;
            fileName = "";
            input = new ArrayList<>();

            while( !readFileSuccess ) {
                System.out.print( "檔案名稱: " );
                fileName = scanner.next();
                input = new ArrayList<Integer>();
                readFileSuccess = G.ReadFile( fileName, input ) ;
                if( !readFileSuccess ) {
                    System.out.println( "找不到檔案" );
                }
            }

            System.out.print( "要切成幾份: " );
            int cut = 0;
            cut = scanner.nextInt();

            long time = 0 ;
            ArrayList<Integer> tmp;

            tmp = new ArrayList<Integer>( input );
            time = BubbleSort.BubbleSortTime( tmp );
            G.Output( tmp, time, fileName, 1 );
            System.out.println("method 1 done");

            tmp = new ArrayList<Integer>( input );
            time = G.ThreadSort( tmp, cut );
            G.Output( tmp, time, fileName, 2 );
            System.out.println("method 2 done");

            tmp = new ArrayList<Integer>( input );
            time = G.MergeBubbleSortTime( tmp, cut );
            G.Output( tmp, time, fileName, 4 );
            System.out.println("method 4 done");

        } // while true
    } // main
}

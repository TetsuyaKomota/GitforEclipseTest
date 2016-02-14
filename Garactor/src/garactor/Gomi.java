package garactor;

public class Gomi {

	/**
	 * @param args
	 */
    public static void main(String[] args) {
        long t = System.currentTimeMillis();



        for (int i = 0; i < 1000 * 1000 * 30; i++) {
        	new Gomi();
        }

        System.out.println((System.currentTimeMillis() - t) + " msec");
    }
}

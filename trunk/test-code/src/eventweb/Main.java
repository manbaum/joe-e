package eventweb;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;
import eventweb.bank.BankServer;

public class Main {

	public static void main(String[] args) {
		serve(System.out);
	}
	
	public static int serve(PrintStream debugOut) {
		final PublicFileServer pfs = new PublicFileServer(new File("/home/adrian/website"));

		final Map<String, Service> services = new HashMap<String, Service>();

		final MonkeyServer monkey = new MonkeyServer();
		services.put("/monkey", monkey);
		final BankServer bank = new BankServer();
		services.put("/bank", bank);
		
		final int SERVE_PORT = 13579;		
		
		try {
			Selector selector = Selector.open();
		
			// Attach selector to server port to listen for incoming connections
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(SERVE_PORT));  // port on default interface (not just local!)
			ssc.configureBlocking(false);
			
			SelectionKey sscKey = ssc.register(selector, SelectionKey.OP_ACCEPT);			
			Map<SelectionKey, Connection> connectionMap = new HashMap<SelectionKey, Connection>();
			
			boolean quit = false;
			while(!quit) {
				int numEvents = selector.select();		
				
				if (numEvents == 0) {
					// TODO: wait a while before polling again?
				} else {
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> iter = keys.iterator();
					while(iter.hasNext()) {
						SelectionKey k = iter.next();
						iter.remove();
						if (k == sscKey) {
							SocketChannel newChan = ssc.accept();
							while (newChan != null) {
								debugOut.println("Accepting connection to " + newChan.socket().toString());
								newChan.configureBlocking(false);
								SelectionKey newKey = newChan.register(selector, SelectionKey.OP_READ);

								connectionMap.put(newKey, 
										new Connection(services, pfs, newChan, debugOut));
								
								// another?
								newChan = ssc.accept();
							}
						} else {
							int status = connectionMap.get(k).newData();
							if (status < 0) {
								k.cancel();
							}
							
							// SelectableChannel sc = (k.channel());
							// if (sc instanceof SocketChannel) {
								
								//processNewData(k, debugOut);
							// } else {
							//	System.out.println("wtf mates? not a socket!");
							// }							
						}
					}
				}
			}
		
			return 0;
		
		} catch (java.io.IOException ioe) {
			ioe.printStackTrace();
			return 10;
		}
	}

	/*
	static void processNewData(SocketChannel sc, PrintStream debugOut) {
		ByteBuffer bb = ByteBuffer.allocate(4096);
		int numBytesRead = sc.read(bb);
		if (numBytesRead > 0) {
			debugOut.println("Read " + numBytesRead + " bytes from " + sc.socket());
			bb.rewind();	// Crucial! argh.
			CharsetDecoder decoder = Charset.forName("US-ASCII").newDecoder();
			CharBuffer result = decoder.decode(bb);
			debugOut.println("Decoded: " + result);
		} else if (numBytesRead < 0) {
			debugOut.println("Peer closed connection to " + sc.socket());
			k.cancel(); // remove from selector.
		}
	}
	*/
}
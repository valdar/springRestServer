package it.andrea.tarocchi.restServer;


public class ServerApp {
//	private static HttpPostClient client;
//	
//	public static void main(String[] args) {
//		ServerApp clientApp = new ServerApp();
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("springApplicationContext.xml");
//
//		applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(clientApp,
//                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true); 
//
//		client.fireRequests();
//	}
	
	private static final int DEFAULT_PORT = 8000;
	
	public static void main(String[] args) throws Exception
    {
		int port = DEFAULT_PORT;
		if(args.length>0){
			try{
				port = Integer.parseInt(args[0]);
			}catch(Exception e){
				port = DEFAULT_PORT;
			}
		}
		
        new ServerApp(port).start();
    }
	
	private WebServer server;
    
    public ServerApp(int port)
    {
        server = new WebServer(port);    
    }
    
    public void start() throws Exception
    {
        server.start();        
        server.join();
    }

}

package br.ufpe.cin.contexto.crowdbikemobile.async;

import java.io.IOException;

import org.apache.commons.lang3.SerializationUtils;

import android.content.Context;
import android.os.AsyncTask;
import br.ufpe.cin.contexto.crowdbikemobile.MainActivity;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class AsyncQueue  extends AsyncTask <String, Void, String> implements  Consumer{
	
    private Channel channel;
    private Connection connection;
    private String endPointName;
    private Context contexto;
    private ConnectionFactory factory;
    
public AsyncQueue(String endPointName, String host, String username, String password, String virtualHost, Context ctx) throws IOException{
	this.endPointName = endPointName;
	this.contexto = ctx;
    //Create a connection factory
	if(factory == null){
		factory = new ConnectionFactory();
	    
	    //hostname of your rabbitmq server
	    factory.setHost(host);
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(virtualHost);
	    //getting a connection
		try {
			 connection = factory.newConnection();	
		} catch (IOException e) {
			// TODO: handle exception
			connection = factory.newConnection();		
		}
		
	    //creating a channel
		if(connection!=null){
	    channel = connection.createChannel();
	    
	    //declaring a queue for this channel. If queue does not exist,
	    //it will be created on the server.
	    channel.queueDeclare(endPointName, true, false, false, null); 
		}
	}
	
}


@Override
protected String doInBackground(String... arg0) {
	// TODO Auto-generated method stub
	try {
		//start consuming messages. Auto acknowledge messages.
		if(channel!=null){
	    	channel.basicConsume(endPointName, true,this);
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	return null;
}

/**
 * Called when consumer is registered.
 */
public void handleConsumeOk(String consumerTag) {
	System.out.println("Consumer "+consumerTag +" registered");	
	
}

/**
 * Called when new message is available.
 */
public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body) throws IOException {
	String result = SerializationUtils.deserialize(body);	
   //((MainActivity) contexto).retornoFila(result);
}

public void handleCancel(String consumerTag) {}
public void handleCancelOk(String consumerTag) {}
public void handleRecoverOk(String consumerTag) {}
public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {}

}


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import processing.core.PApplet;

public class Main extends PApplet {


	private BufferedWriter writer;
	private int pagina;
    private boolean recibio;
    private ArrayList<Usuarios> usersViejos;
    private Usuarios[]usersNuevos;
   
	public static void main(String[] args) {
		PApplet.main("Main");

	}

	public void settings() {
		size(500,500);
	}
	public void setup() {
		pagina=0;
		serverInit();
		recibio=true;
		usersViejos=new ArrayList<Usuarios>();

	}
	public void draw() {
		background(255, 251, 222);
		fill(0);
		textSize(30);

		switch(pagina) {
		case 0:
			text("ingrese usuario y contrasena",50,200);
			break;

		case 1:
			text("Bienvenido",50,200);
			break;

		}
	}



	public void serverInit() {
		new Thread(

				()->{
					try {



						ServerSocket server = new ServerSocket(5000);
						System.out.println("Esperando cliente...");
						Socket socket = server.accept();
						System.out.println("Cliente conectado");


						InputStream is = socket.getInputStream();
						OutputStream out = socket.getOutputStream();


						writer = new BufferedWriter(new OutputStreamWriter(out));
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));


						while(recibio) {
							String line = reader.readLine();
							System.out.println("line"+line);
							Gson gson=new Gson();
							usersNuevos =gson.fromJson(line, Usuarios[].class);
							
							if(usersViejos.isEmpty()) {
								for(int j=0;j<usersNuevos.length;j++) {
									usersViejos.add(usersNuevos[j]);
									System.out.println(usersViejos.get(j).getUser()+usersViejos.get(j).getPass());
								
									if(usersViejos.get(j).equals(usersNuevos[j])) {
										pagina=1;
									}	
								}
							
							}
					
						}



					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				).start();

	}

	public void mousePressed() {
		if(pagina==1) {
			pagina=0;
		}
	}



	public void sendConfirm(String confirm) {
		new Thread(
				()->{
					try {
						writer.write(confirm+"\n");
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				).start();
	}
		
	}







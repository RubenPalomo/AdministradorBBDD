package accesoDatos.UF2;

import com.mysql.jdbc.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class conectionBBDD {
	protected static Connection conect = null;
	protected static Statement peticion;
	protected static ResultSet resultados;
	protected static Scanner entradaDatos = new Scanner(System.in);
	
	public static Connection conexion() {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			conect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/accesodatos", "root", "");
			System.out.println("¡Conectado!\n");
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Error" + e);
		}
		return conect;
	}
	
	public static boolean CrearTabla(String nombreTabla) {
		int resultado;
		
		try {
			peticion = (Statement) conect.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		try {
			resultado = peticion.executeUpdate("CREATE TABLE "+ nombreTabla
					+ "(Nombre varchar(25) NOT NULL, "
					+ "Altura int, "
					+ "PrimeraAscensión int, "
					+ "Región varchar(25), "
					+ "País varchar(25))");
			if (resultado != 0)
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public static boolean InsertarElemento(String nombreTabla, String nombre, int altura, int ascension, String region, String pais) {
		int resultado;
		
		try {
			peticion = (Statement) conect.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		try {
			resultado = peticion.executeUpdate("INSERT INTO `"+ nombreTabla
					+ "`(`Nombre`, `Altura`, `PrimeraAscensión`, `Región`, `País`)"
					+ "VALUES('" + nombre + "', " + altura + ", " + ascension + ", '"+ region + "', '"+ pais + "')");
			if (resultado != 1)
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	public static boolean OpcionInsertarElemento() {
		String nombreTabla, nombre, region, pais;
		int altura, ascension;
		
		try {
			System.out.print("Inserte el nombre de la tabla: ");
			nombreTabla = entradaDatos.nextLine();
			System.out.print("Inserte el nombre: ");
			nombre = entradaDatos.nextLine();
			System.out.print("Inserte la altura: ");
			altura = entradaDatos.nextInt();
			System.out.print("Inserte la fecha de ascensión: ");
			ascension = entradaDatos.nextInt();
			entradaDatos.nextLine();
			System.out.print("Inserte la región: ");
			region = entradaDatos.nextLine();
			System.out.print("Inserte el país: ");
			pais = entradaDatos.nextLine();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Introduzca un número correcto");
			return false;
		}

		return InsertarElemento(nombreTabla, nombre, altura, ascension, region, pais);
	}
	
	public static String LeerTabla() {
		String nombreTabla, elementosTabla = "";
		
		System.out.print("Introduzca el nombre de la tabla: ");
		nombreTabla = entradaDatos.nextLine();
		elementosTabla = "NOMBRE - ALTURA - PRIMERA ASCENSIÓN - REGIÓN - PAÍS\n";
		try {
			peticion = (Statement) conect.createStatement();
			resultados = (ResultSet) peticion.executeQuery("SELECT * FROM " + nombreTabla);

			while (resultados.next()) {
				elementosTabla += (resultados.getString(1) + " - " + resultados.getString(2) + "m - " + resultados.getString(3) 
						+ " - " + resultados.getString(4) + " - " + resultados.getString(5) + "\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error al conectar");
			return "";
		}

		return elementosTabla;
	}
	
	public static String LeerElementoTabla() {
		String nombreTabla, nombre, elementoTabla = "";
		
		System.out.print("Introduzca el nombre de la tabla: ");
		nombreTabla = entradaDatos.nextLine();
		System.out.print("Introduzca el nombre que desee buscar: ");
		nombre = entradaDatos.nextLine();
		try {
			peticion = (Statement) conect.createStatement();
			resultados = (ResultSet) peticion.executeQuery("SELECT * FROM " + nombreTabla + " WHERE `Nombre` = '" + nombre + "'");

			while (resultados.next()) {
				elementoTabla += (resultados.getString(1) + " - " + resultados.getString(2) + "m - " + resultados.getString(3) 
						+ " - " + resultados.getString(4) + " - " + resultados.getString(5) + "\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error al conectar");
			return "";
		}
		if (elementoTabla != "")
			return elementoTabla;
		else
			return "No se ha encontrado nada con ese nombre";
	}
	
	public static boolean EditarElementoTotal(String nombreTabla, String nombreAntiguo, String nombre, int altura, int ascension, String region, String pais) {
		int resultado;
		
		try {
			peticion = (Statement) conect.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		try {
			resultado = peticion.executeUpdate("UPDATE " + nombreTabla
					+ " SET Nombre='" + nombre + "'"
					+ ", Altura = " + altura
					+ ", PrimeraAscensión = " + ascension
					+ ", Región = '" + region + "'"
					+ ", País = '" + pais + "'"
					+ " WHERE Nombre = '" + nombreAntiguo + "'");
			if (resultado != 1)
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	public static boolean EditarElementoParcial(String nombreTabla, String nombre, String fila, String modificacion) {
		int resultado;
		
		try {
			peticion = (Statement) conect.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		try {
			resultado = peticion.executeUpdate("UPDATE " + nombreTabla
					+ " SET " + fila + " = '" + modificacion + "'"
					+ " WHERE Nombre='" + nombre + "'");
			if (resultado != 1)
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	public static boolean OpcionEditarElemento() {
		String opcion, nombreTabla, nombreAntiguo, nombre, altura, ascension, region, pais, fila, modificacion;
		
		System.out.println("¿Desea editar todo un elemento o solo una fila del mismo?");
		opcion = entradaDatos.nextLine();
		if (opcion.contains("todo")) {
			System.out.print("Introduzca el nombre de la tabla: ");
			nombreTabla = entradaDatos.nextLine();
			System.out.print("Introduzca el nombre del elemento a modificar: ");
			nombreAntiguo = entradaDatos.nextLine();
			System.out.print("Introduzca el nuevo nombre: ");
			nombre = entradaDatos.nextLine();
			System.out.print("Introduzca la nueva altura: ");
			altura = entradaDatos.nextLine();
			System.out.print("Introduzca la nueva fecha de ascensión: ");
			ascension = entradaDatos.nextLine();
			System.out.print("Introduzca la nueva región: ");
			region = entradaDatos.nextLine();
			System.out.print("Introduzca el nombre del país: ");
			pais = entradaDatos.nextLine();
			try {
				return EditarElementoTotal(nombreTabla, nombreAntiguo, nombre, Integer.parseInt(altura), Integer.parseInt(ascension), region, pais);					
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		else if (opcion.contains("solo") || opcion.contains("un")) {
			System.out.print("Introduzca el nombre de la tabla: ");
			nombreTabla = entradaDatos.nextLine();
			System.out.print("Introduzca el nombre del elemento a modificar: ");
			nombre = entradaDatos.nextLine();
			System.out.print("Introduzca la fila que desee modificar: ");
			fila = entradaDatos.nextLine();
			System.out.print("Introduzca el nuevo valor: ");
			modificacion = entradaDatos.nextLine();
			return EditarElementoParcial(nombreTabla, nombre, fila, modificacion);
		}
		else {
			System.out.println("No te he entendido.");
			return OpcionEditarElemento();
		}
	}

	public static boolean BorrarElemento(String nombreTabla, String nombre) {
		int resultado;
		
		try {
			peticion = (Statement) conect.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		try {
			resultado = peticion.executeUpdate("DELETE FROM " + nombreTabla
					+ " WHERE Nombre='" + nombre + "'");
			if (resultado != 1)
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	public static boolean BorrarTabla(String nombreTabla) {
		int resultado;
		
		try {
			peticion = (Statement) conect.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		try {
			resultado = peticion.executeUpdate("DROP TABLE " + nombreTabla);
			if (resultado != 0)
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int opcion = 0;
		String nombreTabla;
		
		conexion();
		System.out.println("Bienvenido. ¿Qué desea hacer? Escriba solo el número de la opción:");
		System.out.println("1.	Crear una nueva tabla, que se llame, por ejemplo, “Montañas” (crear tabla). \r\n"
				+ "2.	Añadir una Montaña a la tabla de Montañas (añadir una fila).\r\n"
				+ "3.	Consultar los datos de toda la tabla Montañas (leer tabla).\r\n"
				+ "4.	Consultar los datos de una Montaña (leer fila). \r\n"
				+ "5.	Editar los campos (uno o todos, a elección del usuario) de una Montaña (modificar fila).\r\n"
				+ "6.	Borrar una Montaña, a elección del usuario (eliminar fila).\r\n"
				+ "7.	Borrar la tabla de Montañas (eliminar tabla)\r\n"
				+ "");
		try {
			opcion = entradaDatos.nextInt();
			entradaDatos.nextLine();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("¡Dije solo el número de la opción!");
		}
		switch (opcion) {
		case 1:
			System.out.print("Introduzca el nombre de la tabla a crear: ");
			nombreTabla = entradaDatos.nextLine();
			if (CrearTabla(nombreTabla))
				System.out.println("Tabla creada con éxito.");
			else
				System.out.println("No se ha podido crear la tabla.");
			break;
		case 2:
			if(OpcionInsertarElemento())
				System.out.println("Elemento insertado con éxito.");
			else
				System.out.println("No se ha podido insertar el elemento.");
			break;
		case 3:
			System.out.println(LeerTabla());
			break;
		case 4:
			System.out.println(LeerElementoTabla());
			break;
		case 5:
			if(OpcionEditarElemento())
				System.out.println("Elemento editado.");
			else
				System.out.println("No se ha podido editar el elemento.");
			break;
		case 6:
			System.out.print("Introduzca el nombre de la tabla: ");
			nombreTabla = entradaDatos.nextLine();
			System.out.print("Introduzca el nombre del elemento que desea borrar: ");
			String nombre = entradaDatos.nextLine();
			if(BorrarElemento(nombreTabla, nombre))
				System.out.println("Elemento borrado.");
			else
				System.out.println("No se pudo eliminar el elemento.");
			break;
		case 7:
			System.out.print("Introduzca el nombre de la tabla: ");
			nombreTabla = entradaDatos.nextLine();
			if(BorrarTabla(nombreTabla))
				System.out.println("Tabla borrada.");
			else
				System.out.println("No se pudo borrar la tabla.");
			break;
		default:
			System.err.println("Opción no válida.");
		}
	}
}

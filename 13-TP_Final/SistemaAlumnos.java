/**
 * Importación de clases propias y de Java para manejo de archivos
 */
import misClases.Alumno;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
public class SistemaAlumnos
{
	/**
	 * Objeto "Scanner" global
	 * -Requerido por varios módulos-
	 */
	static Scanner scan = new Scanner(System.in);
	/**
	 * Imprime una línea divisoria
	 * -Requerido por varios módulos-
	 */
	public static void linea()
	{
		System.out.print("\n--------------------------------\n");
	}
	/**
	 * Menú mostrado al inicio del programa
	 * -Requerido por `main()`
	 */
	public static short mostrarMenu()
	{
		boolean valida = false;
		short opcion;
		do {
			linea();
			System.out.print("-------------[MENU]-------------");
			linea();
			System.out.println("1) Cargar información de alumnos");
			System.out.println("2) Pasar de grado a los alumnos");
			System.out.println("3) Ver promedio general de cada grado");
			System.out.println("4) Ver para un grado, la lista de alumnos "
				+ "ordenada por apellido y nombre en forma ascendente");
			System.out.println("5) Ver los datos de los alumnos egresados "
				+ "ordenados según su promedio en forma descendente");
			System.out.println("6) Ver la cantidad de vacantes del colegio");
			System.out.println("7) Ver para un grado y legajo la posicion "
				+ "en la fila");
			System.out.println("0) Salir");
			linea();
			System.out.println("Ingrese una opción: ");
			opcion = scan.nextShort();
			if ((opcion >= 0) && (opcion <= 7))
				valida = true;
		} while (!valida);
		return (opcion);
	}
	/**
	 * Módulo de lectura de datos y carga de matriz de `Alumno`
	 * -Requerido por `main()`-
	 */
	public static void cargarAlumnos(String listaAlumnos, Alumno[][] infoAlumnos)
	{
		/**
		 * Variables para trabajar sobre una línea
		 */
		int inicio;
		int fin;
		String linea = null;
		/**
		 * Manejo de excepciones verificadas
		 */
		try {
			FileReader lectorArchivo = new FileReader(listaAlumnos);
			BufferedReader bufferLectura = new BufferedReader(lectorArchivo);
			/**
			 * Mientras el buffer de lectura tenga algo por leer
			 * desde el archivo, lee la linea
			 */
			while ((linea = bufferLectura.readLine()) != null) {
				/**
				 * `inicio` y `fin` se van actualizando para
				 * obtener los valores requeridos
				 */
				inicio = 0;
				fin = linea.indexOf(";");
				Alumno alumnito;
				// Leer apellido
				String apellido = linea.substring(inicio, fin);
				inicio = fin + 1;
				fin = linea.indexOf(";", inicio);
				// Leer nombre
				String nombre = linea.substring(inicio, fin);
				inicio = fin + 1;
				fin = linea.indexOf(";", inicio);
				// Leer legajo
				int legajo = Integer.parseInt(linea.substring(inicio, fin));
				inicio = fin + 1;
				fin = linea.indexOf(";", inicio);
				// Leer grado
				int grado = Integer.parseInt(linea.substring(inicio, fin));
				inicio = fin + 1;
				// Leer promedio
				int promedio = Integer.parseInt(linea.substring(inicio));
				// Crear el objeto alumno con los datos leidos
				alumnito = new Alumno(apellido,nombre, legajo, grado, promedio);
				// Asigna el alumno a la matriz
				asignarAlumno(infoAlumnos, alumnito);
			}
			// Cerrar el buffer de lectura
			bufferLectura.close();
		} catch (FileNotFoundException excepcion) {
			System.err.println(excepcion.getMessage()
			+ "\n¡El archivo que se quiere leer no existe!");
		} catch (IOException excepcion) {
			System.err.println(
			"\nError leyendo o escribiendo en el archivo");
		}
	}
	/**
	 * Asignación de objeto de clase Alumno a una matriz -según grado-
	 * -Requerido por `cargarAlumnos()`, `pasarDeGrado()`-
	 */
	public static void asignarAlumno(Alumno[][] alumnos, Alumno alumno)
	{
		boolean libre = false;
		int i = 0;
		int grado = alumno.getGrado();
		if ((grado >= 1) && (grado <= 7)) {
			while ((i < alumnos[grado - 1].length) && !libre) {
				if (alumnos[grado - 1][i] == null) {
					alumnos[grado - 1][i] = alumno;
					libre = true;
				}
				i++;
			}
		} else if (grado == -1) {
			// Si el grado es -1, es un egresado
			while ((i < alumnos[0].length) && !libre) {
				if (alumnos[0][i] == null) {
					alumnos[0][i] = alumno;
					libre = true;
				}
				i++;
			}
		}
	}
	/**
	 * Módulo de carga de arreglo de legajos de alumnos desaprobados
	 * -Requerido por `main()`-
	 */
	public static void cargarAlumnosDes(String listaDesaprobados, int[] desaprobados)
	{
		/**
		 * Variables para trabajar sobre una línea
		 */
		int i = 0; // Va a ser el contador de posiciones
		String linea = null;
		/**
		 * Manejo de excepciones verificadas
		 */
		try {
			FileReader lectorArchivo = new FileReader(listaDesaprobados);
			BufferedReader bufferLectura = new BufferedReader(lectorArchivo);
			/**
			 * Mientras el buffer de lectura tenga algo por leer
			 * desde el archivo, lee la línea
			 */
			while ((linea = bufferLectura.readLine()) != null) {
				desaprobados[i] = Integer.parseInt(linea);
				i++;
			}
			// Cerrar el buffer de lectura
			bufferLectura.close();
		} catch (FileNotFoundException excepcion) {
			System.err.println(excepcion.getMessage()
			+ "\n¡El archivo que se quiere leer no existe!");
		} catch (IOException excepcion) {
			System.err.println(
			"\nError leyendo o escribiendo en el archivo");
		}
	}
	/**
	 * Retorna true si la matriz tiene algún valor cargado
	 * -Requerida por `main()`-
	 */
	public static boolean verificarMatrizCargada(Alumno[][] unaMatriz)
	{
		boolean tieneValor = false;
		int i = 0;
		int j;
		while (i < unaMatriz.length && !tieneValor) {
			j = 0;
			while (j < unaMatriz[0].length) {
				if (unaMatriz[i][j] != null)
					tieneValor = true;
				j++;
			}
			i++;
		}
		return (tieneValor);
	}
	/**
	 * Intercambia dos posiciones en un arreglo de ENTEROS
	 * -Requerido por `ordenarRepitentes()`-
	 */
	public static void intercambiar(int[] arreglo, int indice)
	{
		int auxiliar = arreglo[indice];
		arreglo[indice] = arreglo[indice + 1];
		arreglo[indice + 1] = auxiliar;
	}
	/**
	 * Ordena un arreglo de legajos ENTEROS mediante el algoritmo de
	 * ordenamiento de burbuja mejorado, en orden ascendente
	 * -Requerido por `main()`-
	 */
	public static void ordenarRepitentes(int[] legajos)
	{
		boolean ordenado = false;
		int i = legajos.length - 1;
		int j;
		while ((i > 0) && !ordenado) {
			ordenado = true;
			for (j = 0; j < i; j++) {
				if (legajos[j] > legajos[j + 1]) {
					ordenado = false;
					intercambiar(legajos, j);
				}
			}
			i--;
		}
	}
	/**
	 * Ordena un arreglo de alumnos de un grado de forma ascendente según
	 * el orden lexicográfico de la concatenación `nombre`+`apellido`
	 * -Requerido por `mostrarAlumnosPorGrado()`-
	 */
	public static void ordenarPorNombre(Alumno[] unGrado)
	{
		boolean ordenado = false;
		int i = 0;
		int j = 0;
		while ((i < unGrado.length) && (unGrado[i] != null)) {
			j = i + 1;
			while ((j < unGrado.length) && (unGrado[j] != null)) {
				if (unGrado[i].getNombreComp().compareTo(unGrado[j].getNombreComp()) > 0) {
					Alumno auxiliar = unGrado[i];
					unGrado[i] = unGrado[j];
					unGrado[j] = auxiliar;
				}
				j++;
			}
			i++;
		}
	}
	/**
	 * Busca un alumno en el listado de repitentes y retorna TRUE
	 * si fue encontrado (mediante búsqueda binaria)
	 * -Requerido por `pasarDeGrado()`-
	 */
	public static boolean buscarRepitente(int[] repitentes, Alumno unAlumno)
	{
		boolean encontrado = false;
		int inicio = 0;
		int medio;
		int fin = repitentes.length - 1;
		int legajo = unAlumno.getLegajo();
		do {
			medio = (inicio + fin) / 2;
			if (repitentes[medio] == legajo) {
				// `legajo` está justo en el medio
				encontrado = true;
			} else if (repitentes[medio] > legajo) {
				// `legajo` puede estar en el intervalo izquierdo
				fin = medio - 1;
			} else {
				// `legajo` puede estar en el intervalo derecho
				inicio = medio + 1;
			}
		} while (!encontrado && (fin >= inicio));
		return (encontrado);
	}
	/**
	 * Según la existencia de un alumno en el listado de repitentes,
	 * se pasa o no de grado a los alumnos
	 * -Requerido por `main()`-
	 */
	public static Alumno[][] pasarDeGrado(Alumno[][] alumnos, int[] repitentes, Alumno[][] egresados)
	{
		int i;
		int j;
		Alumno[][] actualizados = new Alumno[7][30];
		for (i = 0; i < alumnos.length; i++) {
			j = 0;
			while ((j < alumnos[0].length) && (alumnos[i][j] != null)) {
				Alumno alumnito = alumnos[i][j];
				if (!buscarRepitente(repitentes, alumnito)) {
					// Si no es repitente, se promueve
					alumnito.promover();
				}
				if (alumnito.getGrado() == -1) {
					// Si el alumno egresó, se asigna a la
					// estructura de egresados
					asignarAlumno(egresados, alumnito);
				} else {
					// Si no egresó, va al listado ordinario
					asignarAlumno(actualizados, alumnito);
				}
				j++;
			}
		}
		return (actualizados);
	}
	/**
	 * Cuenta la cantidad de alumnos en un grado -elementos no nulos-
	 * -Requerido por `calcularPromedioGrado()`-
	 */
	public static int contarAlumnosGrado(Alumno[] grado, int contador) {
		int total = 0;
		if ((grado[contador] != null) && (contador < grado.length)) {
			total = 1 + contarAlumnosGrado(grado, contador + 1);
		}
		return (total);
	}
	/**
	 * Calcula el promedio de un grado
	 * -Requerido por `calcularPromedios()`-
	 */
	public static double calcularPromedioGrado(Alumno[] grado, int contador) {
		double total = 0;
		if ((contador < grado.length) && (grado[contador] != null)) {
			total += grado[contador].getPromedioGeneral()
				+ calcularPromedioGrado(grado, contador + 1);
		}
		return (total);
	}
	/**
	 * Calcula el promedio de todos los grados
	 * -Requerido por `main()`-
	 */
	public static void calcularPromedios(Alumno[][] alumnos, double[] promedios, int fila) {
		if (fila < alumnos.length) {
			promedios[fila] = calcularPromedioGrado(alumnos[fila], 0)
				/ contarAlumnosGrado(alumnos[fila], 0);
			calcularPromedios(alumnos, promedios, fila + 1);
		}
	}
	/**
	 * Muestra los promedios calculados por grado
	 * -Requerido por `main()`-
	 */
	public static void mostrarPromedios(double[] promedios) {
		for (int i = 0; i < promedios.length; i++) {
			System.out.println("Promedio del grado "
					+ (i + 1) + ": " + promedios[i]);
		}
	}
	/**
	 * Pide un grado y muestra el listado de alumnos de ese grado en
	 * orden ascendente 
	 * -Requerido por `main()`-
	 */
	public static void mostrarAlumnosPorGrado(Alumno[][] infoAlumnos)
	{
		boolean valido = false;
		do {
			System.out.print("Ingrese un grado: ");
			short grado = scan.nextShort();
			linea();
			if ((grado >= 1) && (grado <= 7)) {
				valido = true;
				Alumno[] alumnosPorNombre = infoAlumnos[grado - 1];
				// Ordena el grado según nombre
				ordenarPorNombre(alumnosPorNombre);
				// Muestra el listado
				for (int i = 0; i < alumnosPorNombre.length; i++) {
					if (alumnosPorNombre[i] != null) {
						System.out.println(alumnosPorNombre[i].toString());
					}
				}
			} else {
				System.out.println("¡Grado inválido!");
			}
		} while (!valido);
	}
	/**
	 * Cuenta la cantidad de elementos no nulos en un arreglo
	 * -Requerido por `ordenarEgresadosQS()`-
	 */
	public static int contarElementos(Alumno[] arreglo)
	{
		int i = 0;
		int contador = 0;
		while ((i < arreglo.length) && (arreglo[i] != null)) {
			contador++;
			i++;
		}
		return (contador);
	}
	/**
	 * Intercambia posiciones de dos elementos en un arreglo de Alumno
	 * -Requerido por `ordenarEgresadosBS()`-
	 */
	public static void intercambiarAlumnos(Alumno[] alumnos, int indice)
	{
		Alumno auxiliar = alumnos[indice];
		alumnos[indice] = alumnos[indice + 1];
		alumnos[indice + 1] = auxiliar;
	}
	/**
	 * Muestra los datos de los alumnos egresados ordenados según su
	 * promedio en forma descendente --[QuickSort]--
	 * -Requerido por `main()`-
	 */
	public static void ordenarEgresadosQS(Alumno[] egresados, int inicio, int fin)
	{
		if (inicio < fin) {
			int pivote = (int)egresados[inicio].getPromedioGeneral();
			int izq = inicio;
			int der = fin;
			Alumno aux;
			while (izq < der) {
				while ((egresados[izq].getPromedioGeneral() >= pivote) && (izq < der)) {
					izq++;
				}
				while (egresados[der].getPromedioGeneral() < pivote) {
					der--;
				}
				if (izq < der) {
					aux = egresados[izq];
					egresados[izq] = egresados[der];
					egresados[der] = aux;
				}
			}
			aux = egresados[inicio];
			egresados[inicio] = egresados[der];
			egresados[der] = aux;
			if (inicio < der - 1) {
				ordenarEgresadosQS(egresados, inicio, der - 1);
			}
			if (der + 1 < fin) {
				ordenarEgresadosQS(egresados, der + 1, fin);
			}
		}
	}
	/**
	 * Muestra los datos de los alumnos egresados ordenados según su
	 * promedio en forma descendente --[BubbleSort Mejorado]--
	 * -Requerido por `main()`-
	 */
	public static void ordenarEgresadosBS(Alumno[] egresados)
	{
		boolean ordenado = false;
		int i = egresados.length;
		int j;
		while ((i > 0) && !ordenado) {
			ordenado = true;
			j = 0;
			while ((j < i) && (egresados[j + 1] != null)) {
				if (egresados[j].getPromedioGeneral()
				< egresados[j + 1].getPromedioGeneral()) {
					intercambiarAlumnos(egresados, j);
					ordenado = false;
				}
				j++;
			}
			i--;
		}
	}
	/**
	 * Muestra los datos de los alumnos egresados
	 * -Requerido por `main()`-
	 */
	public static void mostrarEgresados(Alumno[] egresados)
	{
		int i = 0;
		while ((i < egresados.length) && (egresados[i] != null)) {
			System.out.println(egresados[i].toString());
			i++;
		}
	}
	/**
	 * Cuenta los espacios nulos en una matriz de `Alumno`
	 * -Requerido por `mostrarVacantes()`-
	 */
	public static int contarVacantes(Alumno[][] alumnos, int i, int j) {
		int vacantes = 0;
		if (i < alumnos.length) {
			if (j < alumnos[0].length) {
				if (alumnos[i][j] == null) {
					// Espacio nulo = vacante
					vacantes = 1 + contarVacantes(alumnos, i, j + 1);
				} else {
					vacantes = contarVacantes(alumnos, i, j + 1);
				}
			} else {
				// Columna vuelve a 0, baja una fila
				vacantes = contarVacantes(alumnos, i + 1, 0);
			}
		}
		return (vacantes);
	}
	/**
	 * Muestra la cantidad de vacantes del colegio -espacios nulos-
	 * -Requerido por `main()`-
	 */
	public static void mostrarVacantes(Alumno[][] alumnos) {
		int vacantes = contarVacantes(alumnos, 0, 0);
		linea();
		System.out.println("La cantidad de vacantes en el colegio es de: " + vacantes);
	}
	/**
	 * Muestra la posición de un alumno según su grado y legajo en la matriz
	 * -Requerido por `main()`-
	 */
	public static void mostrarPosicionAlumno(Alumno[][] alumnos)
	{
		boolean valido = false;
		boolean encontrado = false;
		int i = 0;
		int grado;
		int legajo;
		int indice = -1;
		do {
			System.out.println("Ingrese el grado del alumno: ");
			grado = scan.nextInt();
			if ((grado >= 1) && (grado <= 7)) {
				System.out.println("Ingrese el número de legajo del alumno: ");
				legajo = scan.nextInt();
				if ((legajo >= 1000) && (legajo <= 9999)) {
					valido = true;
					// Buscar el legajo en la matriz
					while ((i < alumnos[grado - 1].length)
						&& (alumnos[grado - 1][i] != null)
						&& !encontrado) {
						if (alumnos[grado - 1][i].getLegajo() == legajo) {
							encontrado = true;
							indice = i;
						}
						i++;
					}
				} else {
					System.out.println("¡Legajo inválido!");
				}
			} else {
				System.out.println("¡Grado Inválido!");
			}
		} while (!valido);
		if (indice != -1) {
			System.out.println(
				"El alumno se encuentra en la posición "
				+ indice + " de la matriz.");
		} else {
			System.out.println(
				"¡El alumno no se ha encontrado en el grado dado!");
		}
	}
	/**
	 * ALGORITMO PRINCIPAL
	 */
	public static void main(String[] args)
	{
		short opcionIn;
		// Rutas relativas a los ficheros de lectura
		String listaAlumnos = "Data/ListaAlumnos.txt";
		String listaDesaprobados = "Data/ListaDesaprobados.txt";
		int[] legajosRepitentes = new int[100];
		double[] promedios = new double[7];
		Alumno[][] infoAlumnos = new Alumno[7][30];
		Alumno[][] egresados = new Alumno[1][30];
		do {
			// Menú
			opcionIn = mostrarMenu();
			switch (opcionIn) {
			case 0:
				linea();
				System.out.println("¡Hasta luego! XD");
				break;
			case 1:
				// Cargar datos en las estructuras
				cargarAlumnos(listaAlumnos, infoAlumnos);
				cargarAlumnosDes(listaDesaprobados, legajosRepitentes);
				// Ordenar el listado de repitentes
				ordenarRepitentes(legajosRepitentes);
				linea();
				System.out.println("¡Alumnos cargados!");
				break;
			case 2:
				// Pasar de grado
				if (!verificarMatrizCargada(infoAlumnos)) {
					linea();
					System.out.println("¡Primero debe cargar la información!");
				} else {
					// Cada vez que se pase de grado,
					// habrá nuevos egresados
					egresados = new Alumno[1][30];
					linea();
					infoAlumnos = pasarDeGrado(infoAlumnos
					, legajosRepitentes, egresados);
					linea();
					System.out.println("¡Hecho!");
				}
				break;
			case 3:
				// Mostrar promedio general de cada grado
				if (!verificarMatrizCargada(infoAlumnos)) {
					linea();
					System.out.println("¡Primero debe cargar la información!");
				} else {
					linea();
					calcularPromedios(infoAlumnos, promedios, 0);
					mostrarPromedios(promedios);
				}
				break;
			case 4:
				// Mostrar la lista de alumnos de un grado
				// ordenada por nombres en forma ascendente
				if (!verificarMatrizCargada(infoAlumnos)) {
					System.out.println("¡Primero debe cargar la información!");
				} else {
					mostrarAlumnosPorGrado(infoAlumnos);
				}
				break;
			case 5:
				// Ver los datos de los alumnos egresados
				if (!verificarMatrizCargada(infoAlumnos)) {
					System.out.println("¡Primero debe cargar la información!");
				} else if (!verificarMatrizCargada(egresados)) {
					System.out.println("¡Aún no hay egresados!");
				} else {
					int cantAlumnos = contarElementos(egresados[0]);
					long inicio = 0;
					long fin = 0;
					linea();
					// Métodos de ordenamiento y toma de tiempos
					inicio = System.nanoTime();
					//ordenarEgresadosBS(egresados[0]);
					ordenarEgresadosQS(egresados[0], 0, cantAlumnos - 1);
					fin = System.nanoTime();
					// Ver los datos de los alumnos egresados
					mostrarEgresados(egresados[0]);
					linea();
					System.out.println("Tiempo de ejecución: "
						+ (fin - inicio) + " nanosegundos / "
						+ ((fin - inicio) / Math.pow(10, 9))
						+ " segundos.");
				}
				break;
			case 6:
				// Ver la cantidad de vacantes del colegio
				if (!verificarMatrizCargada(infoAlumnos)) {
					System.out.println("¡Primero debe cargar la información!");
				} else {
					mostrarVacantes(infoAlumnos);
				}
				break;
			case 7:
				// Ver la posición de un alumno en la matriz
				if (!verificarMatrizCargada(infoAlumnos)) {
					System.out.println("¡Primero debe cargar la información!");
				} else {
					mostrarPosicionAlumno(infoAlumnos);
				}
				break;
			}
		} while (opcionIn != 0);
	}
}

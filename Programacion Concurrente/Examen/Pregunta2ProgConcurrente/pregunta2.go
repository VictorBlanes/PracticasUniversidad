/*
	Pregunta 2 Examen programacion concurrente (2021/2022).
	4 amigos han pedido sushi para cenar y se les ha dado una bandeja con un numero aleatorio de unos tipos de sushi.
	Se pide programar mediante canales go un servidor que reciba las peticiones de los hilos amigos y devuelva permisos
	para comer, ademas de controlar cuando se acaba el sushi y finalizas los hilos amigos.
*/
package main

import (
	"fmt"
	"math/rand"
	"time"
)

const (
	cantidadAmigos = 4
)

// Struct que representa un tipo de sushi, guarda el nombre de este sushi y la cantidad.
type PiecaSushi struct {
	tipo string
	n    int
}

// Este struct solo se usa para enviar al canal que controla cuando acaban los hilos
type Empty struct{}

var bandejaSushi = []PiecaSushi{}
var nombreAmigos = []string{"Jaume", "Joan", "LLucia", "Maria"}

/*
	Funcion proveedor
	Se encarga de dar permisos a los amigos cuando estos piden para comer, ademas avisa a todos los
	amigos cuando se ha acabado el sushi y asi finalizar la ejecucion.
	Atributos
		done: Se envia al acabar la funcion, se usa para tener control de que hilos acaban
		peticiones: Por ese canal los hilos amigos enviaran peticiones para poder comer.
					Es un int que es igual al id del hilo hijo
		amigos: Por estos canales el proveedor enviara la confirmacion de peticion para comer.
				Devolvera false si ya no hay sushi
		cogido: Por este canal el hilo amigo indica que sushi ha cogido.
*/
func proveedor(done chan Empty, peticionesComer chan int, permisosComer [cantidadAmigos]chan bool, sushiCogido chan int) {
	for len(bandejaSushi) > 0 {
		amigo := <-peticionesComer
		permisosComer[amigo] <- true
		indice := <-sushiCogido
		if bandejaSushi[indice].n == 0 {
			fmt.Printf("%s ha acabado con %s!!!\n", nombreAmigos[amigo], bandejaSushi[indice].tipo)
			bandejaSushi = append(bandejaSushi[:indice], bandejaSushi[indice+1:]...)
			fmt.Printf("Quedan %d\n", len(bandejaSushi))
		}
	}
	for i := 0; i < cantidadAmigos; i++ {
		permisosComer[i] <- false
	}
	fmt.Printf("Se ha acabado el sushi!!!\n")
	done <- Empty{}
}

/*
	Funcion amigo
	Cada goroutine de esta funcion representa un amigo, va pidiendo para comer al proveedor hasta que
	no queda sushi. Se añade una espera para promover el intercalado entre hilos.
	Atributos:
		id: Identificador numero del hilo
		respuesta: 	Por este canal el hilo recibira la confirmacion para poder comer. Si no queda sushi
					recibe false
		done: Se envia al acabar la funcion, se usa para tener control de que hilos acaban
		peticiones: Por ese canal los hilos amigos enviaran peticiones para poder comer.
					Es un int que es igual al id del hilo hijo
		cogido: Por este canal el hilo amigo indica que sushi ha cogido.
*/
func amigo(id int, done chan Empty, permisosComer chan bool, sushiCogido chan int, peticionesComer chan int) {
	fmt.Printf("Hola mi nombre es %s y vengo a comer sushi\n", nombreAmigos[id])
	sePuedeComer := true
	for sePuedeComer {
		peticionesComer <- id
		sePuedeComer = <-permisosComer
		if sePuedeComer {
			objetoCogido := rand.Intn(len(bandejaSushi))
			sushiCogido <- objetoCogido
			bandejaSushi[objetoCogido].n--
			fmt.Printf("%s coge un %s, quedan %d\n", nombreAmigos[id], bandejaSushi[objetoCogido].tipo, bandejaSushi[objetoCogido].n)
		}
		time.Sleep(200 * time.Millisecond)
	}
	done <- Empty{}
}

/*
	Funcion main
	Inicializa la bandeja de sushi con valores aleatorios, tambien inicializa los canales que se van a usar,
	lanza las goroutines y espera a que terminen.
*/
func main() {
	fmt.Printf("COMIENZA LA SIMULACION\n")
	var nombres = []string{"nigiri de salmon", "shashimi de atun", "maki de cangrejo", "shashimi de anguila", "nigiris de tortilla"}
	rand.Seed(time.Now().UTC().UnixNano())

	fmt.Printf("La bandeja tiene:\n")
	for i := 0; i < len(nombres); i++ {
		bandejaSushi = append(bandejaSushi, PiecaSushi{nombres[i], 1 + rand.Intn(10)})
		fmt.Printf("%d piecas de %s\n", bandejaSushi[i].n, bandejaSushi[i].tipo)
	}
	fmt.Printf("Que aproveche!\n")

	done := make(chan Empty, 1)
	peticionesComer := make(chan int, cantidadAmigos)

	// Sincrono porque el proveedor necesita saber que sushi ha sushiCogido para despues comprobar si se ha acabado
	sushiCogido := make(chan int)
	// Un canal por cada hilo amigo, sincrono porque los hilos amigos tienen que esperar a que el proveedor les de permiso para comer.
	var permisosComer [cantidadAmigos]chan bool
	for i := range permisosComer {
		permisosComer[i] = make(chan bool)
	}

	go proveedor(done, peticionesComer, permisosComer, sushiCogido)

	for i := 0; i < cantidadAmigos; i++ {
		go amigo(i, done, permisosComer[i], sushiCogido, peticionesComer)
	}

	for i := 0; i < cantidadAmigos+1; i++ {
		<-done
	}

	fmt.Printf("End\n")
}

/* Practica 3 programacion concurrente
 * Simulacion del problema del oso de las abejas con RabbitMq y Go, en el jarron puede caber 10 unidades de miel y el oso come 3 veces de el.
 * La simulacion tiene que funcionar tanto para 3 como 3000 abejas.
 *
 * Link video: https://youtu.be/ddeCWTyAurQ
 * Autor: Victor Manuel Blanes Castro
 */
package main

import (
	"log"
	"strconv"
	"strings"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"
)

const CAP_POT = 10
const MAX_ITER = 3

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}

func main() {
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	// Queue donde se ponen los permisos para producir miel, los pone el oso y los recoge las abejas
	q1, err := ch.QueueDeclare(
		"osoAbeja", // name
		false,      // durable (the queue will survive a broker restart)
		true,       // delete when unused
		false,      // exclusive (used by only one connection and the queue will be deleted when that connection closes)
		false,      // no-wait (the server will not respond to the method. The client should not wait for a reply method)
		nil,        // arguments (Those are provided by clients when they declare queues (exchanges) and control various optional features, such as queue length limit or TTL.)
	)
	failOnError(err, "Failed to declare a queue")

	// Queue en donde la abeja encargada pondra el wake para despertar al oso cuando el jarron este lleno.
	q2, err := ch.QueueDeclare(
		"abejaOso", // name
		false,      // durable (the queue will survive a broker restart)
		true,       // delete when unused
		false,      // exclusive (used by only one connection and the queue will be deleted when that connection closes)
		false,      // no-wait (the server will not respond to the method. The client should not wait for a reply method)
		nil,        // arguments (Those are provided by clients when they declare queues (exchanges) and control various optional features, such as queue length limit or TTL.)
	)
	failOnError(err, "Failed to declare a queue")

	msgs2, err := ch.Consume(
		q2.Name, // queue
		"",      // consumer
		true,    // auto-ack
		false,   // exclusive
		false,   // no-local
		false,   // no-wait
		nil,     // args
	)
	failOnError(err, "Failed to register a consumer")

	//Se envia un mensaje inicial para el oso envie los 10 primeros permisos.
	body := "Start"
	err = ch.Publish(
		"",      // exchange
		q2.Name, // routing key
		false,   // mandatory
		false,
		amqp.Publishing{
			DeliveryMode: amqp.Persistent,
			ContentType:  "text/plain",
			Body:         []byte(body),
		},
	)
	failOnError(err, "Failed to publish a message")

	// Goroutine que se encarga de leer el mensaje de wake para que el oso envie los permisos para hacer miel.
	// Ademas cuando el oso haya comido del jarron MAX_ITER veces enviara el mensaje para finalizar la simulacion.
	forever := make(chan bool)
	go func() {
		ind := 0
		for d := range msgs2 {
			body := strings.Split(string(d.Body), " ")
			if body[0] == "Wake" {
				ind++
				log.Printf("%s ha despertado al oso y comienza a comer del jarron %d/%d", body[1], ind, MAX_ITER)
				t := time.Duration(5)
				log.Printf(". . . . .")
				time.Sleep(t * time.Second)
				if ind == MAX_ITER {
					log.Printf("El oso esta lleno y ha roto el jarron!")
					body := "End"
					err = ch.Publish(
						"fin", // exchange
						"",    // routing key
						false, // mandatory
						false,
						amqp.Publishing{
							DeliveryMode: amqp.Persistent,
							ContentType:  "text/plain",
							Body:         []byte(body),
						},
					)
					failOnError(err, "Failed to publish a message")
					forever <- true
				}
				log.Printf("El oso se va a dormir")
			}
			for i := 0; i < CAP_POT; i++ {
				t := time.Duration(250)
				time.Sleep(t * time.Millisecond)
				body := strconv.Itoa(i + 1)
				err = ch.Publish(
					"direct", // exchange
					q1.Name,  // routing key
					false,    // mandatory
					false,
					amqp.Publishing{
						DeliveryMode: amqp.Persistent,
						ContentType:  "text/plain",
						Body:         []byte(body),
					},
				)
				failOnError(err, "Failed to publish a message")
			}
		}
	}()
	log.Printf("El oso duerme si no le dan de comer")
	<-forever
	log.Printf("SIMULACION ACABADA")
}

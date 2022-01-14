package main

import (
	"log"
	"os"
	"strconv"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"
)

const CAP_POT = 10

var ABEJA_NAME = os.Args[1]

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

	q1, err := ch.QueueDeclare(
		"osoAbeja", // name
		false,      // durable (the queue will survive a broker restart)
		true,       // delete when unused
		false,      // exclusive (used by only one connection and the queue will be deleted when that connection closes)
		false,      // no-wait (the server will not respond to the method. The client should not wait for a reply method)
		nil,        // arguments (Those are provided by clients when they declare queues (exchanges) and control various optional features, such as queue length limit or TTL.)
	)
	failOnError(err, "Failed to declare a queue")

	q2, err := ch.QueueDeclare(
		"abejaOso", // name
		false,      // durable (the queue will survive a broker restart)
		true,       // delete when unused
		false,      // exclusive (used by only one connection and the queue will be deleted when that connection closes)
		false,      // no-wait (the server will not respond to the method. The client should not wait for a reply method)
		nil,        // arguments (Those are provided by clients when they declare queues (exchanges) and control various optional features, such as queue length limit or TTL.)
	)
	failOnError(err, "Failed to declare a queue")

	q3, err := ch.QueueDeclare(
		"",    // name
		false, // durable (the queue will survive a broker restart)
		true,  // delete when unused
		false, // exclusive (used by only one connection and the queue will be deleted when that connection closes)
		false, // no-wait (the server will not respond to the method. The client should not wait for a reply method)
		nil,   // arguments (Those are provided by clients when they declare queues (exchanges) and control various optional features, such as queue length limit or TTL.)
	)
	failOnError(err, "Failed to declare a queue")

	err = ch.ExchangeDeclare(
		"fin",    // name
		"fanout", // type
		false,    // durable
		true,     // auto-deleted
		false,    // internal
		false,    // no-wait
		nil,      // arguments
	)
	failOnError(err, "Failed to declare an exchange")

	err = ch.ExchangeDeclare(
		"direct", // name
		"direct", // type
		false,    // durable
		true,     // auto-deleted
		false,    // internal
		false,    // no-wait
		nil,      // arguments
	)
	failOnError(err, "Failed to declare an exchange")

	err = ch.QueueBind(
		q3.Name, // queue name
		"",      // routing key
		"fin",   // exchange
		false,
		nil,
	)
	failOnError(err, "Failed to bind a queue")

	err = ch.QueueBind(
		q2.Name,  // queue name
		q2.Name,  // routing key
		"direct", // exchange
		false,
		nil,
	)
	failOnError(err, "Failed to bind a queue")

	err = ch.QueueBind(
		q1.Name,  // queue name
		q1.Name,  // routing key
		"direct", // exchange
		false,
		nil,
	)
	failOnError(err, "Failed to bind a queue")

	err = ch.Qos(
		1,
		0,
		false,
	)

	msgs1, err := ch.Consume(
		q1.Name, // queue
		"",      // consumer
		false,   // auto-ack
		false,   // exclusive
		false,   // no-local
		false,   // no-wait
		nil,     // args
	)
	failOnError(err, "Failed to register a consumer")

	msgs2, err := ch.Consume(
		q3.Name, // queue
		"",      // consumer
		true,    // auto-ack
		false,   // exclusive
		false,   // no-local
		false,   // no-wait
		nil,     // args
	)
	failOnError(err, "Failed to register a consumer")

	forever := make(chan bool)

	go func() {
		var ABEJA_NAME = os.Args[1]
		for d := range msgs1 {
			log.Printf("%s produce la miel %s", ABEJA_NAME, d.Body)
			value, _ := strconv.Atoi(string(d.Body))
			t := time.Duration(3)
			log.Printf(". . .")
			time.Sleep(t * time.Second)
			d.Ack(false)
			if value == CAP_POT {
				log.Printf("%s despierta al oso", ABEJA_NAME)
				body := "Wake " + ABEJA_NAME
				err = ch.Publish(
					"direct", // exchange
					q2.Name,  // routing key
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

	go func() {
		for range msgs2 {
			log.Printf("El jarron esta roto, no se puede llenar de miel!")
			forever <- true
		}
	}()

	log.Printf(" [*] Esta es la %s", ABEJA_NAME)
	<-forever
	log.Printf("%s se va", ABEJA_NAME)
	log.Printf("SIMULACION ACABADA")
}

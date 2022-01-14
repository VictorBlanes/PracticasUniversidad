package main

import (
	"log"
	"os"
	"strconv"
	"strings"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"
)

const CAP_POT = 10
const MAX_ITER = 3

type messageData struct {
}

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}

func bodyFrom(args []string) string {
	var s string
	if (len(args) < 2) || os.Args[1] == "" {
		s = "hello"
	} else {
		s = strings.Join(args[1:], " ")
	}
	return s
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

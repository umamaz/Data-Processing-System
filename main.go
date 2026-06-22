package main

import (
	"fmt"
	"log"
	"os"
	"sync"
	"time"
)

// ---------------- TASK ----------------

type Task struct {
	ID   int
	Data string
}

// ---------------- TASK QUEUE ----------------

type TaskQueue struct {
	tasks chan Task
}

func NewTaskQueue(size int) *TaskQueue {
	return &TaskQueue{
		tasks: make(chan Task, size),
	}
}

func (q *TaskQueue) AddTask(task Task) {
	q.tasks <- task
}

func (q *TaskQueue) GetTask() (Task, bool) {
	task, ok := <-q.tasks
	return task, ok
}

func (q *TaskQueue) Close() {
	close(q.tasks)
}

// ---------------- SHARED RESULTS ----------------

var (
	results []string
	mutex   sync.Mutex
)

// ---------------- WORKER ----------------

func worker(
	id int,
	queue *TaskQueue,
	wg *sync.WaitGroup,
) {

	defer wg.Done()

	log.Printf(
		"Worker-%d started",
		id,
	)

	for {

		task, ok :=
			queue.GetTask()

		if !ok {
			break
		}

		log.Printf(
			"Worker-%d processing Task-%d",
			id,
			task.ID,
		)

		time.Sleep(
			time.Duration(
				500+task.ID%3*500,
			) * time.Millisecond,
		)

		// Simulated error

		if task.ID == 10 {

			log.Printf(
				"Worker-%d ERROR processing Task-%d",
				id,
				task.ID,
			)

			continue
		}

		result :=
			fmt.Sprintf(
				"Task %d processed by Worker-%d",
				task.ID,
				id,
			)

		mutex.Lock()

		results =
			append(
				results,
				result,
			)

		mutex.Unlock()

		log.Println(result)
	}

	log.Printf(
		"Worker-%d completed",
		id,
	)
}

// ---------------- FILE WRITER ----------------

func saveResults() error {

	file, err :=
		os.Create(
			"resultsGo.txt",
		)

	if err != nil {
		return err
	}

	defer file.Close()

	for _, result := range results {

		_, err :=
			file.WriteString(
				result + "\n",
			)

		if err != nil {
			return err
		}
	}

	return nil
}

// ---------------- MAIN ----------------

func main() {

	queue :=
		NewTaskQueue(20)

	var wg sync.WaitGroup

	for i := 1; i <= 4; i++ {

		wg.Add(1)

		go worker(
			i,
			queue,
			&wg,
		)
	}

	for i := 1; i <= 20; i++ {

		queue.AddTask(
			Task{
				ID: i,
				Data: fmt.Sprintf(
					"Data-%d",
					i,
				),
			},
		)
	}

	queue.Close()

	wg.Wait()

	err := saveResults()

	if err != nil {

		log.Printf(
			"File Error: %v",
			err,
		)

	} else {

		log.Println(
			"Results saved to results.txt",
		)
	}

	log.Printf(
		"Successful Results = %d",
		len(results),
	)

	log.Println(
		"Program Completed",
	)
}

package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	_ "github.com/lib/pq"
	"log"
	"net/http"
	"strconv"
)

const (
	port = 8080
)

var (
	db *sql.DB
)

type NewsModel struct {
	Tag string `json:"tag"`
}

type News struct {
	Id       uint64 `json:"-"`
	Message  string `json:"message"`
	Source   string `json:"source"`
	Date     uint64 `json:"date"`
	ObjectId uint64 `json:"-"`
}

func initDB() {
	var err error
	db, err = sql.Open("postgres", "host=192.168.1.2 dbname=bravo_news user=postgres password=postgres sslmode=disable")
	if err != nil {
		log.Fatal(err)
	}
}

func newsHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var model NewsModel
	err := json.NewDecoder(r.Body).Decode(&model)
	if err != nil {
		fmt.Printf("Pasring err %s\n", err.Error())
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	newsList, err := findNewsByTag(model.Tag)
	if err != nil {
		fmt.Printf("error finding news by tag: %s\n", err.Error())
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	jsonNewsList, err := json.Marshal(newsList)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	fmt.Fprintf(w, "%s", string(jsonNewsList))
}

func findNewsByTag(tag string) ([]*News, error) {
	stag := "%#" + tag + "%"
	rows, err := db.Query("SELECT * FROM news_grabber_news WHERE message LIKE $1", stag)
	if err != nil {
		return nil, err
	}

	newsList := make([]*News, 0)
	for rows.Next() {
		news := new(News)

		err = rows.Scan(&news.Id, &news.Message, &news.Source, &news.ObjectId, &news.Date)
		if err != nil {
			return nil, err
		}

		newsList = append(newsList, news)
	}

	return newsList, nil
}

func main() {
	initDB()
	defer db.Close()

	http.HandleFunc("/news", newsHandler)

	fmt.Printf("Starting server on port %d\n", port)
	if err := http.ListenAndServe(":"+strconv.Itoa(port), nil); err != nil {
		log.Fatal(err)
	}
}

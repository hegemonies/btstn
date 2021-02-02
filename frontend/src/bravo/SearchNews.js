import React from "react";
import ViewNews from "./ViewNews";
import createNews from "./NewsMovel";
import {Container} from "@material-ui/core";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import baseApiUrl from "./Consts";
import MoreNewsButton from "./MoreNewsButton";

class SearchNews extends React.Component {
    constructor(props) {
        super(props);
        this.state = {news: [], tag: '', limit: 10, offset: 0};
        this.handleOnChange = this.handleOnChange.bind(this);
        this.findNews = this.findNews.bind(this);
        this.addMoreNews = this.addMoreNews.bind(this);
    }

    defaultState = () => {
        return {news: [], tag: '', limit: 10, offset: 0}
    }

    newsApiUrl = () => {
        if (baseApiUrl[baseApiUrl.length - 1] === '/') {
            return baseApiUrl.substring(0, baseApiUrl.length - 1) + '/news'
        }

        return baseApiUrl + '/news'
    }

    handleOnChange(e) {
        this.setState({tag: e.target.value});
    }

    findNews(e) {
        e.preventDefault();

        if (this.state.tag.length === 0) {
            return;
        }

        this.setState({
            news: this.defaultState().news,
            offset: this.defaultState().offset,
            limit: this.defaultState().limit
        });

        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                tag: this.state.tag,
                pagination: {
                    offset: this.state.offset,
                    limit: this.state.limit
                }
            }),
            keepalive: true,
            mode: 'cors'
        };

        fetch(this.newsApiUrl(), requestOptions)
            .then(result => result.json())
            .then(value => {

                let countMessages = 0

                value.view.map(item => {
                    const newItem = createNews(
                        countMessages++,
                        item.message,
                        item.date,
                        item.source
                    )

                    this.setState({
                        news: this.state.news.concat(newItem),
                        offset: this.state.offset + 1
                    })
                })
            }, error => {
                console.log(error)
            })
    }

    addMoreNews() {
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                tag: this.state.tag,
                pagination: {
                    offset: this.state.offset,
                    limit: this.state.limit
                }
            }),
            keepalive: true,
            mode: 'cors'
        };

        fetch(this.newsApiUrl(), requestOptions)
            .then(result => result.json())
            .then(value => {

                let countMessages = this.state.news.length

                value.view.map(item => {
                    const newItem = createNews(
                        countMessages++,
                        item.message,
                        item.date,
                        item.source
                    )

                    this.setState({
                        news: this.state.news.concat(newItem),
                        offset: this.state.offset + 1
                    })
                })
            }, error => {
                console.log(error)
            })
    }

    render() {
        return (
            <Container>
                <h1>Bravo News</h1>

                <form onSubmit={this.findNews}>
                    <TextField
                        id="outlined-secondary"
                        label="Enter the stock tag"
                        variant="outlined"
                        color="primary"
                        size="small"
                        onChange={this.handleOnChange}
                    />
                    <Button
                        color="primary"
                        size="large"
                        onClick={this.findNews}
                    >
                        Search
                    </Button>
                </form>

                <ViewNews news={this.state.news}/>

                <MoreNewsButton news={this.state.news} addMoreNewsCallback={() => this.addMoreNews()}/>
            </Container>
        );
    }
}

export default SearchNews;

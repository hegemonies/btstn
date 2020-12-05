import React from "react";
import ViewNews from "./ViewNews";
import createNews from "./NewsMovel";
import {Container} from "@material-ui/core";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import baseApiUrl from "./Consts";

class SearchNews extends React.Component {
    constructor(props) {
        super(props);
        this.state = {news: [], tag: ''};
        this.handleOnChange = this.handleOnChange.bind(this);
        this.handleOnSubmit = this.handleOnSubmit.bind(this);
    }

    newsApiUrl = () => {
        if (baseApiUrl[baseApiUrl.length - 1]   === '/') {
            return baseApiUrl.substring(0, baseApiUrl.length - 1) + '/news'
        }

        return baseApiUrl + '/news'
    }

    handleOnChange(e) {
        this.setState({tag: e.target.value});
    }

    handleOnSubmit(e) {
        e.preventDefault();

        if (this.state.tag.length === 0) {
            return;
        }

        this.setState(state => ({
            news: []
        }));

        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({tag: this.state.tag}),
            keepalive: true,
            mode: 'cors'
        };

        fetch(this.newsApiUrl(), requestOptions)
            .then(result => result.json())
            .then(value => {
                let countMessages = 0

                value.view.map(item => {
                    const newItem = createNews(
                        countMessages,
                        item.message,
                        item.date,
                        item.source
                    )

                    this.setState(state => ({
                        news: state.news.concat(newItem)
                    }))

                    countMessages++
                })
            }, error => {
                console.log(error)
            })
    }


    render() {
        return (
            <Container>
                <h1>Bravo News</h1>

                <form onSubmit={this.handleOnSubmit}>
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
                        onClick={this.handleOnSubmit}
                    >
                        Search
                    </Button>
                </form>

                <ViewNews news={this.state.news}/>
            </Container>
        );
    }
}

export default SearchNews;

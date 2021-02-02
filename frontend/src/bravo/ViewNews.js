import React from "react";
import {Grid, List, ListItem, ListItemText, Typography} from "@material-ui/core";

const listStyle = {
    paddingLeft: 0,
    height: 'inherit',
    width: 'inherit',
};

const listItemStyle = {
    borderBottom: '2px solid #3f51b5',
};

const messageStyle = {
    whiteSpace: 'pre-line',
};

function renderDate(timestamp) {
    let datetime = new Date(timestamp * 1000);
    return datetime.toUTCString()
}


class ViewNews extends React.Component {

    renderRow(id, message, date, source) {
        return (
            <ListItem key={id} style={listItemStyle}>
                <ListItemText primary={
                    <Typography style={messageStyle}>
                        {message}
                    </Typography>
                } secondary={renderDate(date) + ' from ' + source}/>
            </ListItem>
        )
    }

    render() {
        if (this.props.news.length === 0) {
            return (
                <div style={{margin: 20}}>
                    <Grid
                        container
                        direction="row"
                        justify="center"
                        alignItems="center"
                    >
                        No news
                    </Grid>
                </div>
            )
        }

        return (
            <div>
                <List
                    className="FixedSizeList"
                    style={listStyle}
                >
                    {this.props.news.map(item => (
                        this.renderRow(item.id, item.message, item.date, item.source)
                    ))}
                </List>
            </div>
        )
    }
}

export default ViewNews;

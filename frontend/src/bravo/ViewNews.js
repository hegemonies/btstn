import React from "react";
import {List, ListItem, ListItemText, Typography} from "@material-ui/core";

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

    // Row = ({index}) => (
    //     <div>
    //         {
    //             this.renderRow(
    //                 this.props.news[index].id,
    //                 this.props.news[index].message,
    //                 this.props.news[index].date,
    //                 this.props.news[index].source
    //             )
    //         }
    //     </div>
    // );

    render() {
        return (
            <div>
                <List
                    className="FixedSizeList"
                    style={listStyle}
                    itemCount={this.props.news.length}
                    // itemSize={40}
                    // height={500}
                    // width={1000}
                >
                    {/*{this.Row}*/}
                    {this.props.news.map(item => (
                        this.renderRow(item.id, item.message, item.date, item.source)
                    ))}
                </List>
            </div>
        )
    }
}

export default ViewNews;

import React from "react";

class ViewNews extends React.Component {

    render() {
        return (
            <ul>
                {this.props.news.map(item => (
                    <li key={item.id}>
                        {item.message}
                        <br/>
                        <div>{(new Date(item.date)).toGMTString()} from {item.source}</div>
                    </li>
                ))}
            </ul>
        )
    }
}

export default ViewNews;


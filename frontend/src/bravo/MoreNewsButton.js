import React from "react";
import {Grid} from "@material-ui/core";
import Button from "@material-ui/core/Button";

class MoreNewsButton extends React.Component {

    render() {
        if (this.props.news.length > 0) {
            return (
                <div style={{marginBottom: 10}}>
                    <Grid
                        container
                        direction="row"
                        justify="center"
                        alignItems="center"
                    >
                        <Button
                            variant="outlined"
                            color="primary"
                            onClick={() => this.props.addMoreNewsCallback()}
                        >
                            More News
                        </Button>
                    </Grid>
                </div>
            )
        }

        return null;
    }

}

export default MoreNewsButton;

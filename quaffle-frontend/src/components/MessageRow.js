'use strict';

import React from 'react';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';

export default class MessageRow extends React.Component {
	render() {
		return (
			<Paper elevation={1} style={ { display: 'block', width: '400px', margin: '10px' } }>
				<Typography variant="headline" component="h5" style={{ margin: '5px' }}>
	          		{ this.props.from }
	        	</Typography>
	        	<Typography component="p" style={{ margin: '5px' }}>
	          		{ this.props.message }
	        	</Typography>
	        </Paper>
        );
	}
};
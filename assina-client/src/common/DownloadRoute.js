import React from 'react';
import {
    Route,
    Redirect
  } from "react-router-dom";
  
  
const DownloadRoute = ({ component: Component, authenticated, ...rest }) => (
    <Route
      {...rest}
      render={props =>
        authenticated ? (
          <Component {...rest} {...props} />
        ) : (
          <Redirect
            to={{
              pathname: '/download',
              state: { from: props.location }
            }}
          />
        )
      }
    />
);
  
export default DownloadRoute
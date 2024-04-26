import React from 'react';
import {
    Route,
    Redirect
  } from "react-router-dom";
  
  
const EditRoute = ({ component: Component, authenticated, ...rest }) => (
    <Route
      {...rest}
      render={props =>
        authenticated ? (
          <Component {...rest} {...props} />
        ) : (
          <Redirect
            to={{
              pathname: '/edit',
              state: { from: props.location }
            }}
          />
        )
      }
    />
);
  
export default EditRoute
import React from 'react';
import {
    Route,
    Redirect
  } from "react-router-dom";
  
  
const UserManagementRoute = ({ component: Component, authenticated, ...rest }) => (
    <Route
      {...rest}
      render={props =>
        authenticated ? (
          <Component {...rest} {...props} />
        ) : (
          <Redirect
            to={{
              pathname: '/userManagement',
              state: { from: props.location }
            }}
          />
        )
      }
    />
);
  
export default UserManagementRoute
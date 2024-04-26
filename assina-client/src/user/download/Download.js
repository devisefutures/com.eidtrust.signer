import React, { Component, useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import './Download.css';
import { sign, createCredential } from '../../util/APIUtils';
import axios from 'axios';
import Alert from 'react-s-alert';
import {ACCESS_TOKEN, API_BASE_URL, CSC_BASE_URL} from '../../constants';
import {
    Route,
    Redirect
  } from "react-router-dom";


class Download extends Component {
    constructor(props) {
        super(props);
        console.log(props);
        this.state = {fileLink: ''};

    }

    handleClick(event) {
        event.preventDefault();
        window.open(event.target.href);
        window.location = '/profile';
    }

    render() {

        return (
            <div className="download-container">
                <div className="container">
                    <a onClick={this.handleClick} href={this.props.location.state.fileLink}>Download signed pdf</a>
                </div>
            </div>
        );
    }
}

export default Download

import React, { Component } from 'react';
import {Redirect} from 'react-router';
import { Link, NavLink } from 'react-router-dom';
import './Profile.css';
import {ACCESS_TOKEN, API_BASE_URL, CSC_BASE_URL} from '../../constants';
import axios from 'axios';
import Alert from 'react-s-alert';

class Profile extends Component {
    constructor(props) {
        super(props);
        console.log(props);
        this.state = {numCredentials: 0, token: ''};
        
        this.state.token = localStorage.getItem(ACCESS_TOKEN);

        const headers = {
	        'Authorization': 'Bearer '+this.state.token
        };

        axios.post(CSC_BASE_URL+'/credentials/list',{}, {
            headers: headers
        }).then(res => {
            this.setState({
                numCredentials: res.data.credentialIDs.length
            })
        }).catch(error => console.log(error));

        if(this.props.currentUser.encodedPIN == null) {
            console.log('NULL');
        }

    }

    handleClick(event) {
        if(this.props.currentUser.role == "ROLE_ADMIN")
            this.props.history.push("/userManagement")
        else
            this.props.history.push("/sign");
    }

    createCredential(event) {

        var tok = localStorage.getItem(ACCESS_TOKEN);

        const headers = {
	        'Authorization': 'Bearer '+tok
        };

        axios.post(API_BASE_URL+'/credentials', {}, {
            headers: headers
        }).then(res => window.location.reload())
            .catch(error => console.log(error));
    }

    render() {
        return (
            <div className="profile-container">
                <div className="container">
                    <div className="profile-info">
                        <div className="profile-avatar">
                            { 
                                this.props.currentUser.imageUrl ? (
                                    <img src={this.props.currentUser.imageUrl} alt={this.props.currentUser.name}/>
                                ) : (
                                    <div className="text-avatar">
                                        <span>{this.props.currentUser.name && this.props.currentUser.name[0]}</span>
                                    </div>
                                )
                            }
                        </div>
                        <div className="profile-name">
                           <h2>{this.props.currentUser.name}</h2>
                           <p className="profile-email">{this.props.currentUser.email}</p>
                        </div>
                        <div>
                            <p>Number of credentials: {this.state.numCredentials}</p>
                        </div>
                        <div>
                            <button onClick={this.createCredential}>Create Credential</button>
                        </div>
                    </div>
                    <div>
                        <div className="sign-document">
                            {
                                (this.props.currentUser.role == "ROLE_ADMIN") ? (
                                    <Link to='/userManagement'>Manage Users</Link>
                                ) : (
                                    <Link to='/sign'>Sign Document</Link>
                                )
                            }
                        </div>
                        <div className="editProfile">
                            {
                                (this.props.currentUser.encodedPIN == null) ? (
                                    <Link to='/edit'>Please enter your PIN</Link>
                                ) : (
                                    <Link to='/edit'>Edit profile</Link>
                                )
                            }
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Profile
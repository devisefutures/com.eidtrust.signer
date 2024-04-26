import React, { Component } from 'react';
import './Edit.css';
import { Link, Redirect } from 'react-router-dom'
import Alert from 'react-s-alert';
import axios from 'axios';
import { ACCESS_TOKEN, API_BASE_URL } from '../../constants';

class Edit extends Component {
    render() {

        return (
            <div className="signup-container">
                <div className="signup-content">
                    <h1 className="edit-title">Edit profile information</h1>
                    <EditForm {...this.props} />
                </div>
            </div>
        );
    }
}

class EditForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: this.props.currentUser.name,
            email: this.props.currentUser.email,
            password: '',
            pin: ''
        }
        console.log(this.props.currentUser);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleInputChange(event) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName] : inputValue
        });
    }

    handleSubmit(event) {
        event.preventDefault();

        const headers = {
	        'Authorization': 'Bearer '+localStorage.getItem(ACCESS_TOKEN)
        };
        console.log(this.state.pin);
        const data = {
            name: this.state.name,
            email: this.state.email,
            plainPIN: this.state.pin,
            plainPassword: this.state.password
        };
        /*const data = new FormData();
        data.append('name', this.state.name);
        data.append('pin', this.state.pin);
        data.append('email', this.state.email);
        data.append('password', this.state.password);*/

        axios.put(API_BASE_URL + '/users/me', data, {
            headers: headers
        }).then(res => {
            console.log(res);
            this.props.history.push({
                pathname: '/profile',
                currentUser: res.data,
                props: {currentUser: res.data}
            });

        })
    }

    render() {

        const provider = this.props.currentUser.provider;

        return (
            <form onSubmit={this.handleSubmit}>
                {
                    (this.props.currentUser.provider == "local") ? (
                        <div className="form-item">
                            <input type="text" name="name"
                                className="form-control" placeholder={this.props.currentUser.name}
                                value={this.state.name} onChange={this.handleInputChange}/>
                        </div>
                    ) : null
                }
                {
                    (this.props.currentUser.provider == "local") ? (
                        <div className="form-item">
                            <input type="email" name="email"
                                className="form-control" placeholder={this.props.currentUser.email}
                                value={this.state.email} onChange={this.handleInputChange}/>
                        </div>
                    ) : null
                }
                {
                    (this.props.currentUser.provider == "local") ? (
                        <div className="form-item">
                            <input type="password" name="password"
                                className="form-control" placeholder="Enter a password only IF you want to change it"
                                value={this.state.password} onChange={this.handleInputChange}/>
                        </div>
                    ) : null
                }
                <div className="form-item">
                    <input type="password" pattern="[0-9][0-9][0-9][0-9]" name="pin" className="form-control" placeholder="Enter a PIN only IF you want to change it"
                            value={this.state.pin} onChange={this.handleInputChange}/>
                </div>
                <div className="form-item">
                    <button type="submit" className="btn btn-block btn-primary" >Submit</button>
                </div>
            </form>

        );
    }
}

export default Edit

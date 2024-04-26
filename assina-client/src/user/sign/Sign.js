import React, { Component, useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import './Sign.css';
import { sign, createCredential } from '../../util/APIUtils';
import axios from 'axios';
import Alert from 'react-s-alert';
import {ACCESS_TOKEN, ASSINA_SA_BASE_URL, CSC_BASE_URL} from '../../constants';
import Select from 'react-dropdown-select';

class Sign extends Component {
    constructor(props) {
        super(props);
        console.log(props);
        this.state = {file: '', msg: '', pin: '', credentials: [], token: '', selectedCredential: ''};

        this.state.token = localStorage.getItem(ACCESS_TOKEN);

        const headers = {
	        'Authorization': 'Bearer '+this.state.token
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.changeCredential = this.changeCredential.bind(this);

        axios.post(CSC_BASE_URL + '/credentials/list',
            {},
            {
                headers: headers
            }).then(res=>{
            if(res.data.credentialIDs.length == 0) {
                console.log("NO CREDENTIALS");
            }
            else {
                this.setState({
                    credentials: res.data.credentialIDs
                })
                console.log(this.state.credentialIDs);
            }
        }).catch(error=>{
            console.log(error);
        })

    }

	onFileChange = (event) => {
		this.setState({
			file: event.target.files[0]
		});
	}

	uploadFileData = (event) => {
		event.preventDefault();

        console.log(this.state.selectedCredential);

        const headers = {
	        'Authorization': 'Bearer '+this.state.token
        };


        if(this.state.file == '') {
            return(
                <div>{Alert.warning("Please select a file")}</div>
            )
        } else if(this.state.pin == '') {
            return(
                <div>{Alert.warning("Please enter your pin")}</div>
            )
        }

        const data = new FormData();
        data.append('file', this.state.file);
        data.append('pin', this.state.pin);
        data.append('credential', this.state.selectedCredential);

        axios.post(ASSINA_SA_BASE_URL + '/signFile', data, {
            headers: headers
        }).then(res => {
            this.props.history.push({
                pathname: '/download',
                state: {fileLink: res.data.fileDownloadUri}
            });

        })

	}

    handleInputChange(event) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName] : inputValue
        });
    }

    changeCredential= (event) => {
        this.setState({
            selectedCredential: event.target.value
        })
        console.log(this.state.selectedCredential);
    }

    render() {
        const {creds} = this.state.credentials;
        return (
            <div className="sign-container">
                <div className="container">
                    <div className="sign-info">
                        <div className="profile-avatar">
                            {
                                (
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
                    </div>
                </div>
                <form>
                    <select onChange={this.changeCredential}>
                        {this.state.credentials .map(c => {
                        return (
                            <option key={c} value={c}> {c} </option>
                        )
                        })}
                    </select>
                    <div>
                        <input type="file" name="file" onChange={this.onFileChange} required/>
                    </div>
                    <div>
                        <input type="password" pattern="[0-9][0-9][0-9][0-9]" name="pin" placeholder="Pin"
                                value={this.state.pin}    onChange={this.handleInputChange} required/>
                    </div>
                    <div>
                            <button type="button" className="btn" onClick={this.uploadFileData}>Upload</button>
                    </div>
                </form>
            </div>
        );
    }
}

export default Sign

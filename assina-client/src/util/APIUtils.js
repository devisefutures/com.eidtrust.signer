import axios from 'axios';
import {
    ASSINA_RSSP_BASE_URL,
    API_BASE_URL,
    CSC_BASE_URL,
    ACCESS_TOKEN,
    ASSINA_SA_BASE_URL
} from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response =>
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};

export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    });
}

export function login(loginRequest) {
    return request({
        url: ASSINA_RSSP_BASE_URL + "/auth/login",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: ASSINA_RSSP_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function createCredential(token) {
    const headers = {
        'Authorization': 'Bearer '+token
    };

    return axios.post(API_BASE_URL+'/credentials',
            {},
            {
                headers: headers
            }).then(res => {
                console.log(res);
            }).catch(error => {
                console.log(error);
            });
}

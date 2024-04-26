import React, { Component } from 'react';
import './Home.css';
import MediaQuery from 'react-responsive';

class Home extends Component {

    render() {
        return (
            <div style={{ width: '100%', margin: 'auto'}} className="home-container">
                <div className="container">
                    <div className="graf-bg-container">
                        <div className="graf-layout">
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                            <div className="graf-circle"></div>
                        </div>
                    </div>
                    <h1 className="home-title">Login or Signup to start using Assina to sign documents</h1>
                </div>
            </div>
        )
    }
}

export default Home;

import React, {useEffect, useState} from 'react';
import '../css/LoginPage.css';
import {login_v2} from "../api/authService";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";

const LoginPage = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const user = useSelector(state => state.authReducer.user);

    const handleLogin = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError(null);

        try {
            //const data = await login(username, password,dispatch);
            const data = await login_v2(username, password,dispatch);
        } catch (err) {
            setError(err.message);
        } finally {
            setIsSubmitting(false);
        }
    };


    // Используем useEffect для отслеживания изменений user
    useEffect(() => {
        if (user) {
            navigate("/listVideo");
        }
    }, [user]);

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleLogin}>
            <h2 className="login-title">Sign In to Your Account</h2>
                <div className="input-group">
                    <label htmlFor="username">Username</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Enter your username"
                        disabled={isSubmitting}
                    />
                </div>
                <div className="input-group">
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Enter your password"
                        disabled={isSubmitting}
                    />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit" className="login-button" disabled={isSubmitting}>
                    {isSubmitting ? 'Logging in...' : 'Login'}
                </button>
            </form>
        </div>
    );
};

export default LoginPage;
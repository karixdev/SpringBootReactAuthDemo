import {useRef, useState} from "react";
import axios from "axios";
import {buildUriAuth} from '../api/apiUriBuilder'
import {Alert} from "react-bootstrap";
import {emailValidator, passwordValidator} from "../utils/validators";
import ErrorAlert from "../components/ErrorAlert";

export default function Register() {
  const containerStyles = {
    width: "60%",
    margin: "0 auto",
  };

  const email = useRef(null);
  const password = useRef(null);
  const repeatPassword = useRef(null);

  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const validate = (email, password, repeatPassword) => {
    return emailValidator(email) && passwordValidator(password) && password === repeatPassword;
  }

  const resetCredentials = () => {
    email.current.value = '';
    password.current.value = '';
    repeatPassword.current.value = '';
  }

  const register = (payload) => {
    axios.post(buildUriAuth('/register'), payload)
    .then(() => {
      resetCredentials();

      setError('');
      setSuccess(true);
    })
    .catch(err => {
      const status = err.response.status;

      if (status === 409) {
        setError('User with this email already exists')
      } else if (status === 400) {
        setError('Please provide valid credentials')
      } else {
        setError('Something went wrong. Please try again later')
      }
    });
  }

  const formSubmitHandler = e => {
    e.preventDefault();

    setSuccess(false);

    const emailVal = email.current.value;
    const passwordVal = password.current.value;
    const repeatPasswordVal = repeatPassword.current.value;

    if (!validate(emailVal, passwordVal, repeatPasswordVal)) {
      setError('Please provide valid credentials')
      return;
    }

    setError('');

    const payload = {
      email: emailVal,
      password: passwordVal
    };

    register(payload);
  };

  return (
    <div style={containerStyles}>
      {error.length > 0 && (
        <ErrorAlert message={error}/>
      )}
      {success && (
        <Alert variant="success">
          Success! Account created successfully.
        </Alert>
      )}
      <form onSubmit={formSubmitHandler}>
        <div className="form-group">
          <label htmlFor="email">Email address</label>
          <input type="email" className="form-control" id="email" placeholder="Email" ref={email} />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input type="password" className="form-control" id="password" placeholder="Password" ref={password} />
        </div>
        <div className="form-group">
          <label htmlFor="repeat-password">Password</label>
          <input type="password" className="form-control" id="repeat-password" placeholder="Repeat password" ref={repeatPassword} />
        </div>
        <button type="submit" className="btn btn-primary">Register</button>
      </form>
    </div>
  );
}
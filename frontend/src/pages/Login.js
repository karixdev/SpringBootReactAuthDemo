import axios from "axios";
import { useRef, useState } from "react"
import { Alert } from "react-bootstrap";
import { buildUriAuth } from "../api/apiUriBuilder";

export default function Login() {
	const email = useRef(null);
	const password = useRef(null);

	const [error, setError] = useState('');

	const retrieveJWT = (payload) => {
		axios.post(buildUriAuth('/login'), payload)
		.then(resp => {
			if (resp.data?.access_token === undefined) {
				setError('Something went wrong. Please try again later');
				return;
			}
			
			console.log(resp.data.access_token);
		})
		.catch(err => {
			setError(err.response.status === 401 ? 
				'Invalid credentials' : 
				'Something went wrong. Please try again later');
		})
	}

	const formSubmitHandler = e => {
		e.preventDefault();

		setError('');

		const payload = {
			email: email.current.value,
			password: password.current.value
		}

		retrieveJWT(payload);
	}
	
	return (
		<div>
			{error.length > 0 && (
				<Alert variant="danger">
					{error}
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
        <button type="submit" className="btn btn-primary">Register</button>
      </form>
		</div>
	)
}
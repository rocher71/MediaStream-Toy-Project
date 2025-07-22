import "./App.css";

function App() {
	return (
		<>
			<div className='horizontal-start-box'>
				<input type='button' value='버튼' />
			</div>
			<div className='relative-container'>
				<video
					style={{
						width: "640px",
						height: "320px",
						background: "black",
						objectFit: "contain",
					}}
				/>
			</div>
		</>
	);
}

export default App;

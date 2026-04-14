🏨 Hotel Management System (JavaFX)

A desktop-based Hotel Management System built using JavaFX to manage rooms, bookings, and customers through an interactive and user-friendly graphical interface.

🚀 Overview

This project automates basic hotel operations such as:
	•	Room management
	•	Customer booking
	•	Checkout and billing

It provides a structured and efficient way to handle hotel data without manual record-keeping.

✨ Features

🛏️ Room Management
	•	Add new rooms with:
	•	Room Number
	•	Room Type
	•	Price per Day
	•	View room availability status


👤 Customer Management
	•	Enter customer details:
	•	Name
	•	Contact Number
	•	Room Number
	•	Display booking details


🔄 Booking & Checkout
	•	Book available rooms
	•	Prevent duplicate bookings
	•	Checkout with bill calculation
	•	Automatic room availability update


🔍 Additional Features
	•	🔎 Search rooms by number
	•	📊 Dashboard (Total / Available / Occupied rooms)
	•	📈 Sort rooms by price
	•	💰 Receipt generation during checkout
	•	🌙 Dark mode toggle
	•	✅ Input validation & error handling


🛠️ Tech Stack
	•	Language: Java
	•	GUI Framework: JavaFX
	•	Concepts Used:
	•	Object-Oriented Programming
	•	Collections (ObservableList)
	•	Event Handling


▶️ How to Run
bash:
cd ~/HotelManagement

javac --module-path /Users/sanagoel/javafx-sdk-21.0.10/lib \
--add-modules javafx.controls,javafx.fxml \
-d out \
src/projectsg/project.java

java --module-path /Users/sanagoel/javafx-sdk-21.0.10/lib \
--add-modules javafx.controls,javafx.fxml \
-cp out \
projectsg.project


📌 Project Highlights
	•	Clean and modular code structure
	•	Real-time room availability tracking
	•	Interactive and user-friendly UI
	•	Enhanced functionality with additional features
	•	Proper validation and error handling

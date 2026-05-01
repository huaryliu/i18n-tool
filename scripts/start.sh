#!/bin/bash

echo "====================================="
echo "abc Internationalization Resource"
echo "Extraction Tool"
echo "====================================="
echo ""

echo "Starting backend server..."
cd "$(dirname "$0")/../backend"
mvn spring-boot:run &
BACKEND_PID=$!

echo "Waiting for backend to start..."
sleep 15

echo ""
echo "Starting frontend server..."
cd "$(dirname "$0")/../frontend"
npm run dev &
FRONTEND_PID=$!

echo ""
echo "====================================="
echo "Both servers should be running now."
echo "Backend: http://localhost:8080"
echo "Frontend: http://localhost:5173"
echo "====================================="
echo ""

sleep 5

# Try to open the browser
if command -v xdg-open > /dev/null; then
    xdg-open http://localhost:5173
elif command -v open > /dev/null; then
    open http://localhost:5173
fi

echo "Press Ctrl+C to stop both servers"

# Handle Ctrl+C to kill both processes
trap "kill $BACKEND_PID $FRONTEND_PID 2>/dev/null; exit" INT TERM

wait

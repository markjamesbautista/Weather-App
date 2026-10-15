import SwiftUI
import shared

class WeatherAppViewModel: ObservableObject {
    @Published var weatherName: String = "Loading..."
    @Published var temperature: String = ""
    @Published var description: String = ""
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil

    private let repository = SharedWeatherRepository()

    func fetchWeather(lat: Double, lon: Double) {
        isLoading = true
        errorMessage = nil
        Task {
            do {
                let response = try await repository.getWeather(lat: lat, long: lon)
                DispatchQueue.main.async {
                    self.weatherName = response.name
                    self.temperature = "\(response.main.temp)°C"
                    self.description = response.weather.first?.description ?? ""
                    self.isLoading = false
                }
            } catch {
                DispatchQueue.main.async {
                    self.errorMessage = error.localizedDescription
                    self.isLoading = false
                }
            }
        }
    }
}

struct ContentView: View {
    @StateObject private var viewModel = WeatherAppViewModel()
    @State private var latText: String = "14.9968"
    @State private var lonText: String = "121.1710"
    private let platformName = Platform().name

    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Text("Weather Application")
                    .font(.largeTitle)
                    .fontWeight(.bold)

                Text("Running on: \(platformName)")
                    .font(.subheadline)
                    .foregroundColor(.gray)

                HStack(spacing: 12) {
                    TextField("Latitude", text: $latText)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .keyboardType(.decimalPad)
                    TextField("Longitude", text: $lonText)
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .keyboardType(.decimalPad)
                }
                .padding(.horizontal)

                Button(action: {
                    if let lat = Double(latText), let lon = Double(lonText) {
                        viewModel.fetchWeather(lat: lat, lon: lon)
                    }
                }) {
                    Text("Get Weather")
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .cornerRadius(10)
                }
                .padding(.horizontal)

                if viewModel.isLoading {
                    ProgressView("Fetching weather...")
                        .padding()
                } else if let error = viewModel.errorMessage {
                    Text("Error: \(error)")
                        .foregroundColor(.red)
                        .padding()
                } else {
                    VStack(spacing: 10) {
                        Text(viewModel.weatherName)
                            .font(.title)
                            .fontWeight(.semibold)
                        Text(viewModel.temperature)
                            .font(.system(size: 48, weight: .bold))
                        Text(viewModel.description.capitalized)
                            .font(.headline)
                            .foregroundColor(.secondary)
                    }
                    .padding()
                    .background(Color(.systemGray6))
                    .cornerRadius(15)
                    .padding(.horizontal)
                }

                Spacer()
            }
            .navigationTitle("Weather (iOS)")
            .onAppear {
                viewModel.fetchWeather(lat: 14.9968, lon: 121.1710)
            }
        }
    }
}

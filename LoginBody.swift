class LoginBody {
    let email: String
    let password: String
    var description: String {
        return "{\(self.email), password: \(password)}"
    }

    init(email: String, password: String) {
        self.email = email
        self.password = password
    }

    func toDictionnary() -> [String: Any] {
        return ["email": self.email, "password": self.password]
    }
}

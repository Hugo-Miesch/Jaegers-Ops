import UIKit

class LogoutViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Déconnexion"
        view.backgroundColor = .white

        let button = UIButton(type: .system)
        button.setTitle("Se déconnecter", for: .normal)
        button.addTarget(self, action: #selector(logoutTapped), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(button)
        NSLayoutConstraint.activate([
            button.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            button.centerYAnchor.constraint(equalTo: view.centerYAnchor)
        ])
    }

    @objc func logoutTapped() {
        // Nettoie session + retourne au login
        UserDefaults.standard.removeObject(forKey: "API_SESSION")
        UIApplication.shared.windows.first?.rootViewController = UINavigationController(rootViewController: LoginViewController())
    }
}

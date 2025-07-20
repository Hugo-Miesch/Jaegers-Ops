//
//  LoginViewController.swift
//  iZoo-Admin
//
//  Created by Luca Guilliere on 17/02/2025.
//

import UIKit

class LoginViewController: UIViewController {

    @IBOutlet weak var UserTF: UITextField!
    @IBOutlet weak var PassTF: UITextField!
    @IBOutlet weak var ConnectB: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    @IBAction func ConnectHandling(_ sender: Any) {
        guard let u = self.UserTF.text, !u.isEmpty,
              let p = self.PassTF.text, !p.isEmpty else {
            showAlert(message: "Veuillez entrer un identifiant et un mot de passe.")
            return
        }

        let body = LoginBody(email: u, password: p)
        AuthService.login(body: body) { session, err in
            DispatchQueue.main.async {
                if let _ = session {
                    print("[LoginViewController] Connexion réussie")

                    // Transition vers l'écran principal
                    let homeVC = MainTabController()
                    UIView.transition(with: self.navigationController!.view, duration: 0.33, options: .transitionFlipFromRight) {
                        self.navigationController?.viewControllers = [homeVC]
                    }
                } else {
                    self.showAlert(message: "Identifiants incorrects. Veuillez réessayer.")
                }
            }
        }
    

            }
            
            func showAlert(message: String) {
                let alert = UIAlertController(title: "Erreur", message: message, preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                present(alert, animated: true, completion: nil)
            }
        }

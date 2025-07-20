import UIKit

class PartsRequestViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Demande de pièces"
        view.backgroundColor = .systemGray6

        let label = UILabel()
        label.text = "Ici tu pourras faire des demandes de pièces 👨‍🔧"
        label.numberOfLines = 0
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(label)
        NSLayoutConstraint.activate([
            label.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            label.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 20),
            label.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20)
        ])
    }
}

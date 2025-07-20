import UIKit

class TicketDetailViewController: UIViewController {

    let ticket: Ticket

    init(ticket: Ticket) {
        self.ticket = ticket
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private let stackView = UIStackView()
    private let closeButton = UIButton(type: .system)

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Détail du ticket"
        view.backgroundColor = .white
        setupLayout()
        populateDetails()
    }

    func setupLayout() {
        stackView.axis = .vertical
        stackView.spacing = 16
        stackView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(stackView)

        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 20),
            stackView.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 20),
            stackView.rightAnchor.constraint(equalTo: view.rightAnchor, constant: -20)
        ])

        var config = UIButton.Configuration.filled()
        config.title = "Clôturer le ticket"
        config.baseBackgroundColor = .systemRed
        config.baseForegroundColor = .white
        config.cornerStyle = .medium
        config.contentInsets = NSDirectionalEdgeInsets(top: 12, leading: 20, bottom: 12, trailing: 20)
        closeButton.configuration = config

    }

    func populateDetails() {
        let details = [
            "Problème: \(ticket.issue)",
            "Jaeger: \(ticket.jaeger_name)",
            "Entrepôt: \(ticket.entrepot_name)",
            "Priorité: \(ticket.priority)",
            "Statut: \(ticket.status)",
            "Créé le: \(formatDate(ticket.created_at))",
            ticket.resolved_at != nil ? "Résolu le: \(formatDate(ticket.resolved_at!))" : nil
        ].compactMap { $0 }

        for detail in details {
            let label = UILabel()
            label.text = detail
            label.numberOfLines = 0
            stackView.addArrangedSubview(label)
        }

        if ticket.status == "OUVERT" {
            stackView.addArrangedSubview(closeButton)
        }
    }

    @objc func closeTicket() {
        guard let url = URL(string: "\(HttpEnv.host)/tech/tickets/\(ticket.id)/close") else {
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Erreur réseau :", error)
                return
            }

            DispatchQueue.main.async {
                let alert = UIAlertController(title: "Ticket clôturé", message: "Le ticket a été fermé.", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default) { _ in
                    self.navigationController?.popViewController(animated: true)
                })
                self.present(alert, animated: true, completion: nil)
            }
        }.resume()
    }

    func formatDate(_ isoString: String) -> String {
        let isoFormatter = ISO8601DateFormatter()
        let displayFormatter = DateFormatter()
        displayFormatter.dateStyle = .medium
        displayFormatter.timeStyle = .short

        if let date = isoFormatter.date(from: isoString) {
            return displayFormatter.string(from: date)
        } else {
            return isoString
        }
    }
}

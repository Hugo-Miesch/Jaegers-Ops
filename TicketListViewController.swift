// TicketListViewController.swift

import UIKit

class TicketListViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {

    let tableView = UITableView()
    var tickets: [Ticket] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Tickets à traiter"
        view.backgroundColor = .white
        setupTableView()
        loadTicketsFromAPI()
    }

    func setupTableView() {
        tableView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(tableView)
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.topAnchor),
            tableView.leftAnchor.constraint(equalTo: view.leftAnchor),
            tableView.rightAnchor.constraint(equalTo: view.rightAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])

        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "TicketCell")
    }

    func loadTicketsFromAPI() {
        guard let url = URL(string: "\(HttpEnv.host)/tech/tickets") else {
            print("URL invalide")
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Erreur réseau :", error)
                return
            }

            guard let data = data else {
                print("Pas de données reçues")
                return
            }

            do {
                let tickets = try JSONDecoder().decode([Ticket].self, from: data)
                DispatchQueue.main.async {
                    self.tickets = tickets
                    self.tableView.reloadData()
                }
            } catch {
                print("Erreur de parsing JSON :", error)
            }
        }.resume()
    }


    // MARK: - UITableView

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tickets.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let ticket = tickets[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "TicketCell", for: indexPath)
        cell.textLabel?.text = "[\(ticket.priority)] \(ticket.jaeger_name)\n\(ticket.issue)\n→ \(ticket.entrepot_name)"
        cell.textLabel?.numberOfLines = 0
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let ticket = tickets[indexPath.row]
        let detailVC = TicketDetailViewController(ticket: ticket)
        navigationController?.pushViewController(detailVC, animated: true)
    }
}

import UIKit

class OrderListViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {

    let tableView = UITableView()
    var orders: [Order] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Commandes"
        view.backgroundColor = .white
        setupTableView()
        fetchOrders()
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
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "OrderCell")
    }

    func fetchOrders() {
        guard let url = URL(string: "\(HttpEnv.host)/tech/orders") else { return }
        var request = URLRequest(url: url)
        request.httpMethod = "GET"

        URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                print("Erreur réseau :", error)
                return
            }
            guard let data = data else {
                print("Pas de données reçues")
                return
            }

            do {
                let orders = try JSONDecoder().decode([Order].self, from: data)
                DispatchQueue.main.async {
                    self.orders = orders
                    self.tableView.reloadData()
                }
            } catch {
                print("Erreur de parsing JSON :", error)
            }
        }.resume()
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return orders.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let order = orders[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "OrderCell", for: indexPath)
        let piecesText = order.pieces.joined(separator: ", ")
        cell.textLabel?.numberOfLines = 0
        cell.textLabel?.text =
        """
        Commande #\(order.id)
        Jaeger : \(order.jaeger_name)
        Problème : \(order.ticket_issue)
        Pièces : \(piecesText)
        Status : \(order.status)
        Date : \(order.created_at.prefix(10))
        """
        return cell
    }
}

import UIKit

class CreateOrderViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate, UITableViewDataSource {

    let ticketPicker = UIPickerView()
    let tableView = UITableView()
    let addButton = UIButton(type: .system)
    let submitButton = UIButton(type: .system)

    var availableTickets: [Ticket] = []
    var selectedTicket: Ticket?
    var selectedPieces: [String] = []
    let allPieces = ["Réacteur MK3", "Bras hydraulique gauche", "Module radar", "Jambe droite renforcée", "Câblage blindé", "Coque titane avant"]

    let technicienId: Int

    init(technicienId: Int) {
        self.technicienId = technicienId
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Nouvelle commande"
        view.backgroundColor = .white

        ticketPicker.dataSource = self
        ticketPicker.delegate = self
        tableView.dataSource = self

        tableView.translatesAutoresizingMaskIntoConstraints = false
        ticketPicker.translatesAutoresizingMaskIntoConstraints = false
        addButton.translatesAutoresizingMaskIntoConstraints = false
        submitButton.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(ticketPicker)
        view.addSubview(tableView)
        view.addSubview(addButton)
        view.addSubview(submitButton)

        NSLayoutConstraint.activate([
            ticketPicker.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            ticketPicker.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            ticketPicker.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            ticketPicker.heightAnchor.constraint(equalToConstant: 120),

            addButton.topAnchor.constraint(equalTo: ticketPicker.bottomAnchor, constant: 16),
            addButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),

            tableView.topAnchor.constraint(equalTo: addButton.bottomAnchor, constant: 16),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: submitButton.topAnchor, constant: -16),

            submitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -16),
            submitButton.centerXAnchor.constraint(equalTo: view.centerXAnchor)
        ])

        addButton.setTitle("+ Ajouter une pièce", for: .normal)
        addButton.addTarget(self, action: #selector(addPiece), for: .touchUpInside)

        submitButton.setTitle("Créer commande", for: .normal)
        submitButton.addTarget(self, action: #selector(submitCommande), for: .touchUpInside)

        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "PieceCell")

        loadTickets()
    }

    func loadTickets() {
        guard let url = URL(string: "\(HttpEnv.host)/tech/tickets") else { return }
        URLSession.shared.dataTask(with: url) { data, _, _ in
            guard let data = data,
                  let fetched = try? JSONDecoder().decode([Ticket].self, from: data) else { return }
            DispatchQueue.main.async {
                self.availableTickets = fetched.filter { $0.status == "OUVERT" }
                self.ticketPicker.reloadAllComponents()
                if let first = self.availableTickets.first {
                    self.selectedTicket = first
                }
            }
        }.resume()
    }

    @objc func addPiece() {
        let alert = UIAlertController(title: "Ajouter une pièce", message: nil, preferredStyle: .actionSheet)
        for piece in allPieces {
            alert.addAction(UIAlertAction(title: piece, style: .default, handler: { _ in
                self.selectedPieces.append(piece)
                self.tableView.reloadData()
            }))
        }
        alert.addAction(UIAlertAction(title: "Annuler", style: .cancel))
        present(alert, animated: true)
    }

    @objc func submitCommande() {
        guard let ticket = selectedTicket else { return }

        let payload: [String: Any] = [
            "ticket_id": ticket.id,
            "technicien_id": technicienId,
            "pieces": selectedPieces
        ]

        guard let url = URL(string: "\(HttpEnv.host)/tech/orders"),
              let data = try? JSONSerialization.data(withJSONObject: payload) else { return }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = data

        URLSession.shared.dataTask(with: request) { data, _, _ in
            DispatchQueue.main.async {
                self.navigationController?.popViewController(animated: true)
            }
        }.resume()
    }

    // MARK: - UIPickerView

    func numberOfComponents(in pickerView: UIPickerView) -> Int { return 1 }

    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return availableTickets.count
    }

    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        let ticket = availableTickets[row]
        return "[#\(ticket.id)] \(ticket.jaeger_name) - \(ticket.issue)"
    }

    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        selectedTicket = availableTickets[row]
    }

    // MARK: - UITableView

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return selectedPieces.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PieceCell", for: indexPath)
        cell.textLabel?.text = selectedPieces[indexPath.row]
        return cell
    }
}

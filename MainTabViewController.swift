import UIKit

class MainTabController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let ticketVC = UINavigationController(rootViewController: TicketListViewController())
        ticketVC.tabBarItem = UITabBarItem(title: "Tickets", image: UIImage(systemName: "wrench.and.screwdriver"), tag: 0)

        let partsVC = UINavigationController(rootViewController: PartsRequestViewController())
        partsVC.tabBarItem = UITabBarItem(title: "Pièces", image: UIImage(systemName: "shippingbox"), tag: 1)

        let logoutVC = LogoutViewController()
        logoutVC.tabBarItem = UITabBarItem(title: "Déconnexion", image: UIImage(systemName: "power"), tag: 2)

        self.viewControllers = [ticketVC, partsVC, logoutVC]
    }
}

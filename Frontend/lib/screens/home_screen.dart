import 'auth_state.dart';
import 'package:flutter/material.dart';
import 'children_screen.dart';
import 'donate_screen.dart';
import 'visit_screen.dart';
import 'login_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {

  void _goToLogin() async {
    final loggedIn = await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const LoginScreen()),
    );

    if (loggedIn == true) {
      setState(() {});
    }
  }

  @override
  Widget build(BuildContext context) {
    final isLoggedIn = AuthState.isLoggedIn;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Orphanage Home'),
        centerTitle: true,
        actions: [
          if (!isLoggedIn)
            TextButton(
              onPressed: _goToLogin,
              child: const Text('Login', style: TextStyle(color: Colors.white)),
            )
          else
            TextButton(
              onPressed: () {
                AuthState.logout();
                setState(() {});
              },
              child: const Text('Logout', style: TextStyle(color: Colors.white)),
            ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          children: [
            Text(
              isLoggedIn
                  ? 'Welcome! You are logged in.'
                  : 'You are browsing as a guest.',
              style: const TextStyle(color: Colors.grey),
            ),
            const SizedBox(height: 20),
            Expanded(
              child: GridView.count(
                crossAxisCount: 2,
                crossAxisSpacing: 20,
                mainAxisSpacing: 20,
                children: [
                  _buildMenuCard(
                    context,
                    Icons.child_care,
                    'View Children',
                    Colors.blue,
                    () => Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const ChildrenScreen(),
                      ),
                    ),
                  ),
                  _buildMenuCard(
                    context,
                    Icons.card_giftcard,
                    'Make Donation',
                    Colors.green,
                    isLoggedIn
                        ? () => Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (_) => const DonateScreen(),
                              ),
                            )
                        : _goToLogin,
                  ),

                  _buildMenuCard(
                    context,
                    Icons.calendar_today,
                    'Book Visit',
                    Colors.orange,
                    isLoggedIn
                        ? () => Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (_) => const VisitScreen(),
                              ),
                            )
                        : _goToLogin,
                  ),

                  _buildMenuCard(
                    context,
                    Icons.info,
                    'About Us',
                    Colors.purple,
                    () => _showAboutDialog(context),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildMenuCard(
    BuildContext context,
    IconData icon,
    String title,
    Color color,
    VoidCallback onTap,
  ) {
    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(15),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CircleAvatar(
              backgroundColor: color.withOpacity(0.2),
              radius: 30,
              child: Icon(icon, size: 30, color: color),
            ),
            const SizedBox(height: 15),
            Text(
              title,
              style: TextStyle(
                fontWeight: FontWeight.w600,
                color: color,
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _showAboutDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (_) => const AlertDialog(
        title: Text('About Us'),
        content: Text(
          'This is a non-profit organization that cares about the wellbeing of children without shelter and guidance.',
        ),
      ),
    );
  }
}

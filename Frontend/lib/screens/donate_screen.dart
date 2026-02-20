import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../models/donation_request.dart';
import '../widgets/custom_text_field.dart';

class DonateScreen extends StatefulWidget {
  const DonateScreen({super.key});

  @override
  State<DonateScreen> createState() => _DonateScreenState();
}

class _DonateScreenState extends State<DonateScreen> {
  final _formKey = GlobalKey<FormState>();
  final _itemNameController = TextEditingController();
  final _quantityController = TextEditingController();
  final _descriptionController = TextEditingController();
  bool _isSubmitting = false;

  Future<void> _submitDonation() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() {
      _isSubmitting = true;
    });

    try {
      final request = DonationRequest(
        itemName: _itemNameController.text,
        quantity: int.parse(_quantityController.text),
        description: _descriptionController.text,
      );

      final result = await ApiService.makeDonation(request);

      if (result['success'] == true) {
        _itemNameController.clear();
        _quantityController.clear();
        _descriptionController.clear();

        if (!mounted) return;
        _showSuccessDialog(result['message']);
      } else {
        if (!mounted) return;
        _showErrorDialog(result['message']);
      }
    } catch (e) {
      if (!mounted) return;
      _showErrorDialog('An error occurred: $e');
    } finally {
      if (mounted) {
        setState(() {
          _isSubmitting = false;
        });
      }
    }
  }

  void _showSuccessDialog(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Row(
          children: [
            Icon(Icons.check_circle, color: Colors.green),
            SizedBox(width: 10),
            Text('Success'),
          ],
        ),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              Navigator.pop(context);
            },
            child: const Text('OK'),
          ),
        ],
      ),
    );
  }

  void _showErrorDialog(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Row(
          children: [
            Icon(Icons.error, color: Colors.red),
            SizedBox(width: 10),
            Text('Error'),
          ],
        ),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('OK'),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    _itemNameController.dispose();
    _quantityController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Make Donation'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Card(
                elevation: 4,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(15),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(20),
                  child: Column(
                    children: [
                      const Icon(
                        Icons.card_giftcard,
                        size: 60,
                        color: Colors.green,
                      ),
                      const SizedBox(height: 20),
                      const Text(
                        'Make a Donation',
                        style: TextStyle(
                          fontSize: 24,
                          fontWeight: FontWeight.bold,
                          color: Colors.green,
                        ),
                      ),
                      const SizedBox(height: 10),
                      Text(
                        'Your donation helps support the children',
                        style: TextStyle(
                          color: Colors.grey[600],
                          fontSize: 14,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 30),
              CustomTextField(
                controller: _itemNameController,
                label: 'Item Name',
                hintText: 'e.g., Clothes, Food, Books, Toys',
                isRequired: true,
              ),
              CustomTextField(
                controller: _quantityController,
                label: 'Quantity',
                hintText: 'e.g., 5, 10, 20',
                isNumber: true,
                isRequired: true,
              ),
              CustomTextField(
                controller: _descriptionController,
                label: 'Description (Optional)',
                hintText: 'Additional details about your donation',
              ),
              const SizedBox(height: 30),
              ElevatedButton(
                onPressed: _isSubmitting ? null : _submitDonation,
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.green,
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
                child: _isSubmitting
                    ? const Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 20,
                            height: 20,
                            child: CircularProgressIndicator(
                              color: Colors.white,
                              strokeWidth: 2,
                            ),
                          ),
                          SizedBox(width: 10),
                          Text('Submitting...'),
                        ],
                      )
                    : const Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(Icons.send),
                          SizedBox(width: 10),
                          Text(
                            'Submit Donation',
                            style: TextStyle(fontSize: 16),
                          ),
                        ],
                      ),
              ),
              const SizedBox(height: 10),
              TextButton(
                onPressed: () => Navigator.pop(context),
                child: const Text('Cancel'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

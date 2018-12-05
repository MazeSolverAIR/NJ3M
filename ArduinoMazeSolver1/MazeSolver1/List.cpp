#include "List.h"
#include "stdlib.h"
#include "iostream.h"
#include "stddef.h"
#include "Arduino.h"

using namespace std;

int brojac = 0;

List::List() {
	head = NULL;
	curr = NULL;
	temp = NULL;
}

void List::AddNode(char addData) {
	nodePtr n = new node;
	n->next = NULL;
	n->data = addData;
	n->elementBroj = brojac;
	brojac++;

	if (head != NULL) {
		curr = head;
		while (curr->next != NULL) {
			curr = curr->next;
		}
		curr->next = n;
	}
	else 
	{
		head = n;

	}

}

void List::DeleteNode(char delData) {
	nodePtr delPtr = NULL;
	temp = head;
	curr = head;
	while (curr != NULL && curr->data != delData) {
		temp = curr;
		curr = curr->next;
	}
	if (curr == NULL) {
		delete delPtr;
	}
	else {
		delPtr = curr;
		curr = curr->next;
		temp->next = curr;
		delete delPtr;

	}
}

int List::brojElemenata() {
	curr = head;
	int brojac = 0;
	while (curr != NULL) {
		brojac++;
	}
	return brojac;
}

char List::PrintElement(int pozicijaElementa) {
	curr = head;
	char trenutnaNaredba;
	for (int i = 0; i<=pozicijaElementa;i++) 
	{
		if (i == pozicijaElementa && i==curr->elementBroj) {
			Serial.print(curr->data);
			trenutnaNaredba = curr->data;
		}
		else {
			curr = curr->next;
		}
		
	}
	return trenutnaNaredba;
}
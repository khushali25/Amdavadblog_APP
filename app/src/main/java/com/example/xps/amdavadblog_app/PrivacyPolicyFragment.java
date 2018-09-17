package com.example.xps.amdavadblog_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicyFragment extends Fragment {

    String page;
    TextView txtprivacy;
    public int PrivacyInstance;
    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        txtprivacy = view.findViewById(R.id.txtprivacy);
        txtprivacy.setText("This Privacy Policy was last modified on February 1, 2018.\n" +
                   "\n" +
                   "Saprek solution (“us”, “we”, “the Company”, or “our”) operates https://www.amdavadbllog.com (the “Site”). This page informs you of our policies regarding the collection, use and disclosure of Personal Information we receive from users of the Site.\n" +
                   "\n" +
                   "We use your Personal Information only for providing and improving the Site. By using the Site, you agree to the collection and use of information in accordance with this policy. Unless otherwise defined in this Privacy Policy, terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, accessible at https://www.amdavadblog.com.\n" +
                   "\n" +
                   "Information Collection And Use\n" +
                   "\n" +
                   "While using our Site, we may ask you to provide us with certain personally identifiable information that can be used to contact or identify you. Personally identifiable information may include, but is not limited to, your name, email address, postal address and phone number (“Personal Information”).\n" +
                   "\n" +
                   "Like many site operators, we collect information that your browser sends whenever you visit our Site (“Log Data”). This Log Data may include information such as your computer’s Internet Protocol (“IP”) address, browser type, browser version, the pages of our Site that you visit, the time and date of your visit, the time spent on those pages and other statistics.\n" +
                   "\n" +
                   "Cookie\n" +
                   "\n" +
                   "Cookies are files with small amount of data, which may include an anonymous unique identifier. Cookies are sent to your browser from a web site and stored on your computer’s hard drive.\n" +
                   "\n" +
                   "Like many sites, we use “cookies” to collect information. You can instruct your browser to refuse all cookies or to indicate when a cookie is being sent. However, if you do not accept cookies, you may not be able to use some portions of our Site.\n" +
                   "\n" +
                   "Security\n" +
                   "\n" +
                   "The security of your Personal Information is important to us, but remember that no method of transmission over the Internet, or method of electronic storage, is 100% secure. While we strive to use commercially acceptable means to protect your Personal Information, we cannot guarantee its absolute security.\n" +
                   "\n" +
                   "Links To Other Sites\n" +
                   "\n" +
                   "Our Site may contain links to other sites that are not operated by us. If you click on a third party link, you will be directed to that third party’s site. We strongly advise you to review the Privacy Policy of every site you visit.\n" +
                   "\n" +
                   "Saprek solution has no control over, and assumes no responsibility for, the content, privacy policies, or practices of any third party sites or services.\n" +
                   "\n" +
                   "Changes To This Privacy Policy\n" +
                   "\n" +
                   "Saprek solution may update this Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on the Site. You are advised to review this Privacy Policy periodically for any changes.\n" +
                   "\n" +
                   "Contact Us\n" +
                   "\n" +
                   "If you have any questions about this Privacy Policy, please contact us.");
        return view;
    }
}

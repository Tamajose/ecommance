import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getCurrentUser } from "../api/userApi";
import { useAuth } from "../context/AuthContext";

function formatDate(value){
    if(!value){
        return "—";
    }

    return new Date(value).toLocaleString();
}

function Field({ label, value }){
    return (
        <div className="profile-field">
            <span className="profile-label">{label}</span>
            <span className="profile-value">{value === null || value === undefined || value === "" ? "—" : value}</span>
        </div>
    );
}

export default function Profile() {
    const { user } = useAuth();
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        let cancelled = false;

        async function load(){
            setLoading(true);
            setError(null);
            try{
                const data = await getCurrentUser();
                if(!cancelled){
                    setProfile(data);
                }
            } catch(err){
                if(!cancelled){
                    setError(err.message || "Failed to load your details");
                }
            } finally{
                if(!cancelled){
                    setLoading(false);
                }
            }
        }

        load();

        return () => {
            cancelled = true;
        };
    }, []);

    const displayName = profile?.name || user?.username || "My profile";
    const initials = displayName.trim().charAt(0).toUpperCase();
    const hasAddress = Boolean(
        profile?.addressLine || profile?.addressCity || profile?.addressPostalCode || profile?.addressCountry
    );
    const canSell = profile?.role === "SELLER" || profile?.role === "ADMIN";

    return (
        <div className="profile-page">
            <div className="profile-card">
                <div className="profile-header">
                    <span className="profile-avatar">{initials}</span>
                    <div className="profile-heading">
                        <h2>{displayName}</h2>
                        <p className="profile-username">@{profile?.username || user?.username}</p>
                    </div>
                    {profile?.role && <span className="profile-role">{profile.role}</span>}
                </div>

                {error && <p className="error">{error}</p>}

                {loading ? (
                    <div className="loading">Loading your details…</div>
                ) : profile ? (
                    <>
                        <h3 className="profile-section-title">Account</h3>
                        <div className="profile-grid">
                            <Field label="Full name" value={profile.name} />
                            <Field label="Username" value={profile.username} />
                            <Field label="Email" value={profile.email} />
                            <Field label="Phone" value={profile.phone} />
                            <Field label="Role" value={profile.role} />
                            <Field label="Status" value={profile.enabled ? "Active" : "Disabled"} />
                            <Field label="Member since" value={formatDate(profile.createdAt)} />
                        </div>

                        <h3 className="profile-section-title">Location</h3>
                        {hasAddress ? (
                            <div className="profile-grid">
                                <Field label="Address" value={profile.addressLine} />
                                <Field label="City" value={profile.addressCity} />
                                <Field label="Postal code" value={profile.addressPostalCode} />
                                <Field label="Country" value={profile.addressCountry} />
                            </div>
                        ) : (
                            <p className="profile-empty">No address saved yet — your address is used at checkout.</p>
                        )}
                    </>
                ) : null}

                <div className="profile-actions">
                    <Link to="/">Back to shop</Link>
                    {canSell && <Link to="/my-products">My products</Link>}
                    {profile?.role === "ADMIN" && <Link to="/admin">Admin dashboard</Link>}
                </div>
            </div>
        </div>
    );
}
